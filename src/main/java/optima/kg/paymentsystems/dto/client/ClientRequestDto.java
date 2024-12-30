package optima.kg.paymentsystems.dto.client;

import lombok.Data;

/**
 * @author Abubakir Dev
 */

public class ClientRequestDto {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
