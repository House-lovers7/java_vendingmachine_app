package com.example.vendingmachine.dto;

import lombok.NonNull;
import lombok.Value;

@Value
public class ErrorDto {

    /**
     * エラーコード
     */
    @NonNull
    private String errCode;

    /**
     * エラーメッセージ
     */
    @NonNull
    private String errMsg;

}
