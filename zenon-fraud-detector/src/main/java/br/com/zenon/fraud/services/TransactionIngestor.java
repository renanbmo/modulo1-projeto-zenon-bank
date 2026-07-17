package br.com.zenon.fraud.services;

import br.com.zenon.fraud.mappers.TransactionMapper;
import br.com.zenon.fraud.models.Transaction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class TransactionIngestor {
    public List<Transaction> getTransactions(String filePath) throws IOException {
        return getTransactions(filePath, Long.MAX_VALUE);
    }

    public List<Transaction> getTransactions(String filePath, long limit) throws IOException {
        var path = Paths.get(filePath);

        List<String> allLines = Files.readAllLines(path);

        return allLines.stream()
                .skip(1) // skip header
                .limit(limit)
                .map(TransactionMapper::mapToTransaction)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }


}
