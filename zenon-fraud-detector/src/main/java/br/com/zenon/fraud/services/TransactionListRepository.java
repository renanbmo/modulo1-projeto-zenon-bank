package br.com.zenon.fraud.services;

import br.com.zenon.fraud.interfaces.TransactionRepositoryInterface;
import br.com.zenon.fraud.models.Transaction;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TransactionListRepository implements TransactionRepositoryInterface {
    private final List<Transaction> transactions;

    public TransactionListRepository(List<Transaction> transactions) {
        Objects.requireNonNull(transactions);

        this.transactions = transactions;
    }

    @Override
    public Optional<Transaction> getTransactionByOriginName(String name){
        return transactions.stream()
                .filter(t -> t.origin().name().equals(name))
                .findFirst();
    }

    @Override
    public boolean saveTransaction(Transaction transaction) {
        return transactions.add(transaction);
    }
}
