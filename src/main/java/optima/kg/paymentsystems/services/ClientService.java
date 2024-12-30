package optima.kg.paymentsystems.services;

import optima.kg.paymentsystems.dto.SimpleResponse;
import optima.kg.paymentsystems.dto.client.ClientRequestDto;
import optima.kg.paymentsystems.dto.client.ClientResponseDto;

import java.util.List;

/**
 * @author Abubakir Dev
 */
public interface ClientService {
    ClientResponseDto saveClient(ClientRequestDto client);

    ClientResponseDto getClientById(Long clientId);

    List<ClientResponseDto> getAllClients();

    SimpleResponse deleteClient(Long clientId);

    ClientResponseDto updateClient(Long clientId, ClientRequestDto clientRequestDto);
}
