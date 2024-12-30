package optima.kg.paymentsystems.junit;

import optima.kg.paymentsystems.dal.entity.Client;
import optima.kg.paymentsystems.dal.repository.ClientRepository;
import optima.kg.paymentsystems.dto.SimpleResponse;
import optima.kg.paymentsystems.dto.client.ClientRequestDto;
import optima.kg.paymentsystems.dto.client.ClientResponseDto;
import optima.kg.paymentsystems.exceptions.AlreadyExistException;
import optima.kg.paymentsystems.exceptions.NotFoundException;
import optima.kg.paymentsystems.services.impl.ClientServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Abubakir Dev
 */
@ExtendWith(MockitoExtension.class)
public class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    void getAllClients_ShouldReturnListOfClients() {
        Client client1 = new Client();
        client1.setId(1L);
        client1.setName("Client 1");

        Client client2 = new Client();
        client2.setId(2L);
        client2.setName("Client 2");

        List<Client> clients = Arrays.asList(client1, client2);

        Mockito.when(clientRepository.findAll()).thenReturn(clients);

        List<ClientResponseDto> result = clientService.getAllClients();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(client1.getName(), result.get(0).getName());
        Assertions.assertEquals(client2.getName(), result.get(1).getName());
    }

    @Test
    void getClientById_ShouldReturnClient_WhenClientExists() {
        Long clientId = 1L;
        Client client = new Client();
        client.setId(clientId);
        client.setName("Client 1");

        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        ClientResponseDto result = clientService.getClientById(clientId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(clientId, result.getId());
        Assertions.assertEquals(client.getName(), result.getName());
    }

    @Test
    void saveClient_ShouldSaveClient_WhenValidInput() {
        ClientRequestDto clientRequestDto = new ClientRequestDto();
        clientRequestDto.setName("Client 1");

        Client client = new Client();
        client.setId(1L);
        client.setName("Client 1");

        Mockito.when(clientRepository.save(Mockito.any(Client.class))).thenReturn(client);

        ClientResponseDto result = clientService.saveClient(clientRequestDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(client.getName(), result.getName());
    }


    @Test
    void getClientById_ShouldThrowNotFoundException_WhenClientDoesNotExist() {
        Long clientId = 1L;

        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            clientService.getClientById(clientId);
        });
        Assertions.assertEquals("Client with id 1 not found", exception.getMessage());
    }



    @Test
    void updateClient_ShouldUpdateClient_WhenClientExists() {
        Long clientId = 1L;
        ClientRequestDto clientRequestDto = new ClientRequestDto();
        clientRequestDto.setName("Updated Client");

        Client existingClient = new Client();
        existingClient.setId(clientId);
        existingClient.setName("Old Client");

        Client updatedClient = new Client();
        updatedClient.setId(clientId);
        updatedClient.setName("Updated Client");

        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.of(existingClient));
        Mockito.when(clientRepository.save(Mockito.any(Client.class))).thenReturn(updatedClient);

        ClientResponseDto result = clientService.updateClient(clientId, clientRequestDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Updated Client", result.getName());
    }


    @Test
    void deleteClient_ShouldDeleteClient_WhenClientExists() {
        Long clientId = 1L;

        Mockito.when(clientRepository.existsById(clientId)).thenReturn(true);

        SimpleResponse result = clientService.deleteClient(clientId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getHttpStatus());
        Assertions.assertTrue(result.getMessage().contains("success deleted"));
    }

    @Test
    void deleteClient_ShouldThrowAlreadyExistException_WhenClientDoesNotExist() {
        Long clientId = 1L;

        Mockito.when(clientRepository.existsById(clientId)).thenReturn(false);

        AlreadyExistException exception = Assertions.assertThrows(AlreadyExistException.class, () -> {
            clientService.deleteClient(clientId);
        });
        Assertions.assertEquals("Client with id 1 not exist", exception.getMessage());
    }
}
