package br.com.zenon.fraud.models;

import java.math.BigDecimal;

public record Customer(String name, BigDecimal balance) {
}
