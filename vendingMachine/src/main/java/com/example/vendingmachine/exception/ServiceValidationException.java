package com.example.vendingmachine.exception;

import lombok.Getter;


/**
 * {@link org.springframework.stereotype.Service} 層のビジネスロジック例外
 */
public final class ServiceValidationException extends RuntimeException {

    /**
     * メッセージコード
     * @note メッセージコードは、<code>messages.properties</code> に定義されている必要があります。
     */
    @Getter
    private final String messageCode;

    @Getter
    private String[] args;

    public ServiceValidationException(final String messageCode) {
        super(messageCode);
        this.messageCode = messageCode;
    }

    public ServiceValidationException(final String messageCode, final String... args) {
        this(messageCode);
        this.args = args;
    }
}
