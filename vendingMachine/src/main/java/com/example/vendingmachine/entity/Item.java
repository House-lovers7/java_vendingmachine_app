package com.example.vendingmachine.entity;

import lombok.Data;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Data
public class Item {

    @Id
    @NotBlank(message = "E_VALIDATION_003")
    @Size(max = 5, message = "E_VALIDATION_004")
    private String itemCd;

    @NotBlank(message = "E_VALIDATION_009")
    @Size(max = 20, message = "E_VALIDATION_010")
    private String itemName;

    @Pattern(regexp = "[0,1]", message = "E_VALIDATION_011")
    private String sellFlg;

}

