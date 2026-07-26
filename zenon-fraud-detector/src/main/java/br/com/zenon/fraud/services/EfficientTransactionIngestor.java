package br.com.zenon.fraud.services;

import br.com.zenon.fraud.mappers.TransactionMapper;
import br.com.zenon.fraud.models.Transaction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class EfficientTransactionIngestor {

    public static final int LINE_BATCH_SIZE = 3_000;

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

    public void readBatchAsStream(String filePath, Consumer<List<Transaction>> consumer) throws IOException {
        var path = Paths.get(filePath);

        try(var executor = Executors.newFixedThreadPool(10)) {
            try (var lines = Files.lines(path).skip(1)) {
                var interator = lines.iterator();

                List<Transaction> buffer = new ArrayList<>(LINE_BATCH_SIZE);
                var size = 0;

                while (interator.hasNext()){
                    String line = interator.next();
                    TransactionMapper.mapToTransaction(line).ifPresent(buffer::add);
                    size++;

                    if (buffer.size() == LINE_BATCH_SIZE) {
                        var batch = new ArrayList<>(buffer);
                        executor.submit(() -> consumer.accept(batch));
                        buffer.clear();
                    }
                }

                if (!buffer.isEmpty()) {
                    consumer.accept(buffer);
                }
            }
        }
    }
}
