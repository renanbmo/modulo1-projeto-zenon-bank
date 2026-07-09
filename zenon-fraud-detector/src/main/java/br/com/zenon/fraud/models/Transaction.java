package br.com.zenon.fraud.models;

import java.math.BigDecimal;

public record Transaction(long step, Type type, BigDecimal amount,
                          String nameOrig, BigDecimal oldbalanceOrg, BigDecimal newbalanceOrig,
                          String nameDest, BigDecimal oldbalanceDest, BigDecimal newbalanceDest,
                          byte isFraud, byte isFlaggedFraud) {

    public enum Type {CASH_IN, CASH_OUT, DEBIT, PAYMENT, TRANSFER}
}
