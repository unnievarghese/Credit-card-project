package com.CardManagement.card.Io.Request;

import lombok.Data;
import javax.validation.constraints.Pattern;

@Data
public class CardRequest {

    @Pattern(regexp = "[0-9]{4}")
    private String pin;

}
