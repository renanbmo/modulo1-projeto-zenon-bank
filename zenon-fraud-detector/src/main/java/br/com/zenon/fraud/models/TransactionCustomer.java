package br.com.zenon.fraud.models;

import java.math.BigDecimal;
import java.util.Objects;

public record TransactionCustomer(String name, BigDecimal oldBalance, BigDecimal newBalance) {
    public TransactionCustomer{
        Objects.requireNonNull(name);
        Objects.requireNonNull(oldBalance);
        Objects.requireNonNull(newBalance);

        if (oldBalance.signum() < 0) throw new IllegalArgumentException("oldBalance cannot be negative: " + oldBalance);
        if (newBalance.signum() < 0) throw new IllegalArgumentException("newBalance cannot be negative: " + newBalance);
        if (name.isBlank()) throw new IllegalArgumentException("name cannot be blank");
    }
}
