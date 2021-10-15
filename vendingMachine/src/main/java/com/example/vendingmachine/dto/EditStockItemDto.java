package com.example.vendingmachine.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.seasar.doma.Id;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class EditStockItemDto {
    @Id
    @NotBlank(message = "E_VALIDATION_001")
    @Size(max = 2, message = "E_VALIDATION_002")
    private String rackNo;

    @NotBlank(message = "E_VALIDATION_003")
    @Size(max = 5, message = "E_VALIDATION_004")
    private String itemCd;

    @NotNull(message = "E_VALIDATION_005")
    @Range(min = 0, max = 99, message = "E_VALIDATION_006")
    private Integer itemNumber;

    @NotNull(message = "E_VALIDATION_007")
    @Range(min = 0, max = 999, message = "E_VALIDATION_008")
    private Integer price;
}
