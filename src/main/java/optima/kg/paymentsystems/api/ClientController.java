package optima.kg.paymentsystems.api;

import optima.kg.paymentsystems.dto.SimpleResponse;
import optima.kg.paymentsystems.dto.client.ClientRequestDto;
import optima.kg.paymentsystems.dto.client.ClientResponseDto;
import optima.kg.paymentsystems.services.ClientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Abubakir Dev
 */
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/save")
    private ClientResponseDto saveClient(@RequestBody ClientRequestDto client) {
        return clientService.saveClient(client);
    }

    @GetMapping("/{clientId}")
    public ClientResponseDto getClient(@PathVariable Long clientId) {
        return clientService.getClientById(clientId);
    }

    @GetMapping
    public List<ClientResponseDto> getAllClients() {
        return clientService.getAllClients();
    }

    @PutMapping("/{clientId}")
    public ClientResponseDto updateClient(@PathVariable Long clientId,
                                          @RequestBody ClientRequestDto clientRequestDto){
        return clientService.updateClient(clientId,clientRequestDto);
    }

    @DeleteMapping("/{clientId}")
    public SimpleResponse deleteClient(@PathVariable Long clientId) {
        return clientService.deleteClient(clientId);
    }
}
