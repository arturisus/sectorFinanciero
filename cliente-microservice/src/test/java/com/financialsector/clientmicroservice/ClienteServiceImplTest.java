package com.financialsector.clientmicroservice;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.*;
import java.io.File;

import com.financialsector.clientmicroservice.domain.Cliente;
import com.financialsector.clientmicroservice.repository.ClienteRepository;
import com.financialsector.clientmicroservice.service.impl.ClienteServiceImpl;
import org.junit.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import util.JsonFileReader;

@RunWith(MockitoJUnitRunner.class)
public class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @Test
    @DisplayName("Obtener todos los clientes")
    public void testGetAllClientes() {
        // Arrange
        List<Cliente> clientes = new ArrayList<>();
        clientes.add(new Cliente());
        when(clienteRepository.findAll()).thenReturn(clientes);

        // Act
        List<Cliente> result = clienteService.getAllClientes();

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Obtener cliente por ID")
    public void testGetClienteById() {
        // Arrange
        Long id = 1L;
        Cliente cliente = new Cliente();
        cliente.setClienteId(id);
        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));

        // Act
        Optional<Cliente> result = clienteService.getClienteById(id);

        // Assert
        assertEquals(id, result.get().getClienteId());
    }

    @Test
    @DisplayName("Crear cliente")
    public void testCreateCliente() throws IOException {
        // Arrange
        File oldJsonFile = new File("src/test/java/util/oldClient.json");
        Cliente client = JsonFileReader.readJsonFile(oldJsonFile, Cliente.class);

        ArgumentCaptor<String> exchangeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> routingKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Map> parametrosCaptor = ArgumentCaptor.forClass(Map.class);

        // Act
        clienteService.createCliente(client);

        // Assert
        verify(rabbitTemplate, times(1)).convertAndSend(
                exchangeCaptor.capture(),
                routingKeyCaptor.capture(),
                parametrosCaptor.capture()
        );
        verify(clienteRepository, times(1)).save(client);
    }

    @Test
    @DisplayName("Actualizar cliente")
    public void testUpdateCliente() {
        // Arrange
        Long id = 1L;
        Cliente cliente = new Cliente();
        cliente.setClienteId(id);

        // Act
        clienteService.updateCliente(id, cliente);

        // Assert
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    @DisplayName("Aplicar cambios a cliente")
    public void testPatchCliente() throws IOException {
        // Arrange
        File oldJsonFile = new File("src/test/java/util/oldClient.json");
        File newJsonFile = new File("src/test/java/util/newClient.json");
        Cliente oldClient = JsonFileReader.readJsonFile(oldJsonFile, Cliente.class);
        Cliente newClient = JsonFileReader.readJsonFile(newJsonFile, Cliente.class);

        when(clienteRepository.findById(anyLong())).thenReturn(Optional.ofNullable(oldClient));

        // Act
        Cliente result = clienteService.patchCliente(anyLong(), newClient);

        // Assert
        assertEquals("contraseñaNueva", result.getContraseña());
        assertEquals("Desactivado", result.getEstado());
    }

    @Test
    @DisplayName("Eliminar cliente")
    public void testDeleteCliente() {
        // Arrange
        Long id = 1L;

        // Act
        clienteService.deleteCliente(id);

        // Assert
        verify(clienteRepository, times(1)).deleteById(id);
    }

}
