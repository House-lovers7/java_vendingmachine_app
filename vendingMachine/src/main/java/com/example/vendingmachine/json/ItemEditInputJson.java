package com.example.vendingmachine.json;

import com.example.vendingmachine.entity.Item;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class ItemEditInputJson {
    @Valid
    @NotEmpty(message = "E_VALIDATION_012")
    private List<Item> itemList;
}
