package com.CardManagement.card.Io.Request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class CardRequest {

    @Pattern(regexp = "[0-9]{4}")
    private String pin;

}
