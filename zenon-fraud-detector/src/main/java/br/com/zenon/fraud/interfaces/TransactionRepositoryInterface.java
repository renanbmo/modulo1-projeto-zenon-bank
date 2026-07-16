package br.com.zenon.fraud.interfaces;

import br.com.zenon.fraud.models.Transaction;

import java.util.Optional;

public interface TransactionRepositoryInterface {
    Optional<Transaction> getTransactionByOriginName(String name);
}
