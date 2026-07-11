package br.com.zenon.fraud.models;

import java.math.BigDecimal;
import java.util.Objects;

public record Transaction(int step, TransactionType type, BigDecimal amount,
                          TransactionCustomer origin,
                          TransactionCustomer recipient,
                          boolean isFraud, boolean isFlaggedFraud) {
    public Transaction{
        Objects.requireNonNull(type);
        Objects.requireNonNull(amount);
        Objects.requireNonNull(origin);
        Objects.requireNonNull(recipient);

        if (step <= 0) throw new IllegalArgumentException("step must be positive: " + step);
        if (amount.signum() < 0) throw new IllegalArgumentException("amount cannot be negative");
    }
}
