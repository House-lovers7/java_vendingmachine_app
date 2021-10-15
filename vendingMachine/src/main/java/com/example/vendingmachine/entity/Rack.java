package com.example.vendingmachine.entity;

import lombok.Data;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;

import javax.validation.constraints.NotBlank;

@Entity
@Data
public class Rack {

    @Id
    @NotBlank(message = "E_VALIDATION_001")
    private String rackNo;

    private String itemCd;

    private Integer itemNumber;

    private Integer capacity;

    private Integer price;
}
