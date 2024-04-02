package com.financialsector.clientmicroservice.service.spec;

import com.financialsector.clientmicroservice.domain.Cliente;

import java.util.List;
import java.util.Optional;

public interface IClienteService {
        List<Cliente> getAllClientes();
        Optional<Cliente> getClienteById(Long id);
        Cliente createCliente(Cliente cliente);
        Cliente updateCliente(Long id, Cliente cliente);
        Cliente patchCliente(Long id, Cliente cliente);
        void deleteCliente(Long id);
}
