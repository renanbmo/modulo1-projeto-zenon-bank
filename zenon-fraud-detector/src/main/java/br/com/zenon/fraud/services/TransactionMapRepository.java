package br.com.zenon.fraud.services;

import br.com.zenon.fraud.interfaces.TransactionRepositoryInterface;
import br.com.zenon.fraud.models.Transaction;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class TransactionMapRepository implements TransactionRepositoryInterface {

    private Map<String,Transaction> transactions;

    public TransactionMapRepository(List<Transaction> transactions) {
        Objects.requireNonNull(transactions);

        this.transactions = transactions.stream()
                .collect(Collectors.toMap(
                        t -> t.origin().name(),
                        t -> t
                ));
    }

    @Override
    public Optional<Transaction> getTransactionByOriginName(String name) {
        var transaction = transactions.get(name);
        return Optional.of(transaction);
    }
}
