package edu.nutech.tht.enums;

public enum TransactionTypeEnum {
    PAYMENT("PAYMENT"),
    TOPUP("TOPUP");

    public final String label;

    private TransactionTypeEnum(String label) {
        this.label = label;
    }
}
