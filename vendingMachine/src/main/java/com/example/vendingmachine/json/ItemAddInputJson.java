package com.example.vendingmachine.json;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ItemAddInputJson {

    @NotBlank(message = "E_VALIDATION_003")
    @Size(max = 5, message = "E_VALIDATION_004")
    private String itemCd;

    @NotBlank(message = "E_VALIDATION_009")
    @Size(max = 20, message = "E_VALIDATION_010")
    private String itemName;
}
