package com.example.vendingmachine.json;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RackBuyInputJson {
    @NotBlank(message = "E_VALIDATION_001")
    @Size(max = 2, message = "E_VALIDATION_002")
    private String rackNo;

}
