package br.com.zenon.fraud.models;

import java.math.BigDecimal;

public record Transaction(int step, TransactionType type, BigDecimal amount,
                          TransactionCustomer origin,
                          TransactionCustomer recipient,
                          boolean isFraud, boolean isFlaggedFraud) {
}
