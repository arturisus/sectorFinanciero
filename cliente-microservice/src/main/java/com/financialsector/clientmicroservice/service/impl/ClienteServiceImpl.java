package com.financialsector.clientmicroservice.service.impl;

import com.financialsector.clientmicroservice.domain.Cliente;
import com.financialsector.clientmicroservice.exception.ClienteNotFoundException;
import com.financialsector.clientmicroservice.repository.ClienteRepository;
import com.financialsector.clientmicroservice.service.spec.IClienteService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClienteServiceImpl implements IClienteService {

    private static final int RANDOM_NUMBER_LENGTH = 16;
    private static final String DEFAULT_ACCOUNT_TYPE = "Corriente";
    private static final double INITIAL_BALANCE = 1000.00;
    private static final String DEFAULT_ACCOUNT_STATUS = "Activa";

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.routingkey}")
    private String routingkey;

    @Override
    public List<Cliente> getAllClientes() {
        return (List<Cliente>) clienteRepository.findAll();
    }

    @Override
    public Optional<Cliente> getClienteById(Long id) {
        return clienteRepository.findById(id);
    }

    @Override
    public Cliente createCliente(Cliente cliente) {
        Map<String, Object> parametros = getParametros(cliente);
        rabbitTemplate.convertAndSend(exchange,routingkey, parametros);
        return clienteRepository.save(cliente);
    }

    private static Map<String, Object> getParametros(Cliente cliente) {
        String randomDigits = generateCardNumber(RANDOM_NUMBER_LENGTH);
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("numeroCuenta", randomDigits);
        parametros.put("tipoCuenta", DEFAULT_ACCOUNT_TYPE);
        parametros.put("saldoInicial", INITIAL_BALANCE);
        parametros.put("saldoTotal", INITIAL_BALANCE);
        parametros.put("estado", DEFAULT_ACCOUNT_STATUS);
        parametros.put("identificacion", cliente.getIdentificacion());
        return parametros;
    }

    @Override
    public Cliente updateCliente(Long id, Cliente cliente) {
        cliente.setClienteId(id);
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente patchCliente(Long id, Cliente cliente) {
        Optional<Cliente> optionalExistingCliente = clienteRepository.findById(id);
        optionalExistingCliente.ifPresentOrElse(
                existingCliente -> {
                    if (cliente.getContraseña() != null) {
                        existingCliente.setContraseña(cliente.getContraseña());
                    }
                    if (cliente.getEstado() != null) {
                        existingCliente.setEstado(cliente.getEstado());
                    }
                    clienteRepository.save(existingCliente);
                },
                () -> {
                    throw new ClienteNotFoundException("El cliente con ID " + id + " no fue encontrado");
                }
        );
        return cliente;
    }

    @Override
    public void deleteCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    private static String generateCardNumber(int length) {
        return new Random().ints(length, 0, 10)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }
}
