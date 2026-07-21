package br.com.zenon.fraud.services;

import br.com.zenon.fraud.interfaces.TransactionRepositoryInterface;
import br.com.zenon.fraud.models.Transaction;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TransactionMapRepository implements TransactionRepositoryInterface {

    private final Map<String,Transaction> transactions;

    public TransactionMapRepository(List<Transaction> transactions) {
        Objects.requireNonNull(transactions);

        this.transactions = transactions.stream()
                .collect(Collectors.toMap(
                        t -> t.origin().name(),
                        Function.identity()
                ));
    }

    @Override
    public Optional<Transaction> getTransactionByOriginName(String name) {
        return Optional.ofNullable(transactions.getOrDefault(name, null));
    }

    @Override
    public boolean saveTransaction(Transaction transaction) {
        var anterior = transactions.putIfAbsent(transaction.origin().name(), transaction);
        return anterior == null;
    }
}
