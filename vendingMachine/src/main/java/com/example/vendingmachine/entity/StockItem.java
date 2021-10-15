package com.example.vendingmachine.entity;

import lombok.Data;
import org.seasar.doma.Entity;

import static org.seasar.doma.jdbc.entity.NamingType.SNAKE_UPPER_CASE;

@Entity(naming = SNAKE_UPPER_CASE)
@Data
public class StockItem {

    private String rackNo;

    private String itemCd;

    private String itemName;

    private Integer itemNumber;

    private Integer price;
}
