package com.banking.model;

public enum TransactionType {
    DEPOSIT("Nạp tiền"),
    WITHDRAWAL("Rút tiền"),
    TRANSFER_IN("Chuyển đến"),
    TRANSFER_OUT("Chuyển đi");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
