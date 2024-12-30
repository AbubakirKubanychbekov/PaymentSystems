package optima.kg.paymentsystems.services.impl;

import optima.kg.paymentsystems.dal.entity.Client;
import optima.kg.paymentsystems.dal.repository.ClientRepository;
import optima.kg.paymentsystems.dto.SimpleResponse;
import optima.kg.paymentsystems.dto.client.ClientRequestDto;
import optima.kg.paymentsystems.dto.client.ClientResponseDto;
import optima.kg.paymentsystems.exceptions.AlreadyExistException;
import optima.kg.paymentsystems.exceptions.NotFoundException;
import optima.kg.paymentsystems.services.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Abubakir Dev
 */
@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * Retrieves all clients from the database.
     *
     * @return List of ClientResponseDto objects containing client details.
     */
    @Override
    public List<ClientResponseDto> getAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }


    /**
     * Retrieves details of a specific client by their ID.
     *
     * @param clientId ID of the client to retrieve.
     * @return A ClientResponseDto containing client details.
     * @throws NotFoundException if the client is not found.
     */
    @Override
    public ClientResponseDto getClientById(Long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow(() ->
                new NotFoundException("Client with id %s not found".formatted(clientId)));
        return mapToResponseDTO(client);
    }

    /**
     * Saves a new client to the database.
     *
     * @param client A ClientRequestDto containing the client's details.
     * @return A ClientResponseDto containing the saved client's details.
     */
    @Override
    public ClientResponseDto saveClient(ClientRequestDto client) {
        Client client1 = new Client();
        client1.setName(client.getName());
        Client client2 = clientRepository.save(client1);
        log.info("Client %s success saved".formatted(client1));
        return mapToResponseDTO(client2);
    }

    /**
     * Updates an existing client details.
     *
     * @param clientId         ID of the client to update.
     * @param clientRequestDto A ClientRequestDto containing the updated details.
     * @return A ClientRequestDto containing the updated client details.
     * @throws NotFoundException if the client with the given ID is not found.
     */
    @Override
    public ClientResponseDto updateClient(Long clientId, ClientRequestDto clientRequestDto) {
        Client client = clientRepository.findById(clientId).orElseThrow(() ->
                new NotFoundException("Client with id %s not found".formatted(clientId)));
        client.setName(clientRequestDto.getName());
        Client updatedClient = clientRepository.save(client);
        log.info("Client with id %s success updated".formatted(clientId));
        return mapToResponseDTO(updatedClient);
    }

    /**
     * Deletes a client with the specified ID.
     *
     * @param clientId ID of the client to delete.
     * @return A SimpleResponse indicating the result of the operation.
     * @throws AlreadyExistException if the client does not exist.
     */
    @Override
    public SimpleResponse deleteClient(Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new AlreadyExistException("Client with id %s not exist".formatted(clientId));
        }
        clientRepository.deleteById(clientId);
        log.info("Client with id %s successfully is deleted".formatted(clientId));
        return new SimpleResponse(HttpStatus.OK,
                "Client with id %s success deleted".formatted(clientId));
    }

    /**
     * Maps a Client entity to a ClientResponseDto.
     *
     * @param client The Client entity to map.
     * @return A ClientResponseDto containing the client's details.
     */
    private ClientResponseDto mapToResponseDTO(Client client) {
        ClientResponseDto dto = new ClientResponseDto();
        dto.setId(client.getId());
        dto.setName(client.getName());
        dto.setCardIds(client.getCards() != null
                ? client.getCards().stream()
                .map(card -> card.getId())
                .collect(Collectors.toList())
                : new ArrayList<>());
        return dto;
    }
}
