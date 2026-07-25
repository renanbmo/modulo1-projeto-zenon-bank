package br.com.zenon.fraud.services;

import br.com.zenon.fraud.mappers.TransactionMapper;
import br.com.zenon.fraud.models.Transaction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class EfficientTransactionIngestor {

    public void readAsStream(String filePath, Consumer<Transaction> consumer) throws IOException {
        readAsStream(filePath, Long.MAX_VALUE, consumer);
    }

    public void readAsStream(String filePath, long limit, Consumer<Transaction> consumer) throws IOException {
        var path = Paths.get(filePath);

        try (var lines = Files.lines(path)) {
            lines.skip(1)
                .limit(limit)
                .map(TransactionMapper::mapToTransaction)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(consumer);
        }
    }

    public void readBatchAsStream(String filePath, int batchSize, Consumer<List<Transaction>> consumer) throws IOException {
        var path = Paths.get(filePath);

        try (var lines = Files.lines(path)) {
            List<Transaction> buffer = new ArrayList<>(batchSize);

            lines.skip(1)
                    .map(TransactionMapper::mapToTransaction)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(t -> {
                        buffer.add(t);
                        if(buffer.size() == batchSize){
                            consumer.accept(new ArrayList<>(buffer));
                            buffer.clear();
                        }
                    });

            if (!buffer.isEmpty()) {
                consumer.accept(buffer);
            }
        }
    }
}
