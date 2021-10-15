package com.example.vendingmachine.json;

import com.example.vendingmachine.dto.EditStockItemDto;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class RackEditInputJson {
    @Valid
    @NotEmpty(message = "E_VALIDATION_013")
    private List<EditStockItemDto> rackItemList;
}
