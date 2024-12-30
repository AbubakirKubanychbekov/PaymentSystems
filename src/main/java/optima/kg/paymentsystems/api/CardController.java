package optima.kg.paymentsystems.api;

import optima.kg.paymentsystems.dal.entity.Card;
import optima.kg.paymentsystems.dto.card.CardRequestDto;
import optima.kg.paymentsystems.dto.card.CardResponseDto;
import optima.kg.paymentsystems.services.CardService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * @author Abubakir Dev
 */
@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/{clientId}/issue")
    public CardResponseDto issueCard(@PathVariable Long clientId,
                                     @RequestBody CardRequestDto card) {
        return cardService.issueCard(clientId, card);
    }

    @PostMapping("/{cardId}/replenishment")
    public CardResponseDto replenishmentCard(@PathVariable Long cardId,
                                             @RequestParam BigDecimal amount){
        return cardService.replenishment(cardId,amount);
    }

    @PostMapping("/{cardId}/withdraw")
    public CardResponseDto withdraw(@PathVariable Long cardId,
                                    @RequestParam BigDecimal amount) {
       return cardService.withdraw(cardId, amount);
    }


}
