package com.example.vendingmachine.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemAddResponseWrapper {

    private final int addedItemNumber;
}
