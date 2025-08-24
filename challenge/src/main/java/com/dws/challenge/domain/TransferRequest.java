package com.dws.challenge.domain;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {

    @NotBlank
    private String accountFromId;

    @NotBlank
    private String accountToId;

    @NotNull
    @Min(value = 1, message = "Transfer amount must be positive.")
    private BigDecimal amount;
}
