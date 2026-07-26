package br.com.zenon.fraud;

import br.com.zenon.fraud.models.Transaction;
import br.com.zenon.fraud.repository.TransactionSQLRepository;
import br.com.zenon.fraud.services.EfficientTransactionIngestor;
import br.com.zenon.fraud.services.TransactionIngestor;
import br.com.zenon.fraud.util.ProgressCounter;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

public class IngestorMain {
    void main() throws IOException {
        var ingestor = new EfficientTransactionIngestor();
        var sqlRepository = new TransactionSQLRepository();

        long initialTime = System.currentTimeMillis();
        IO.println("Starting ingestion...");

        ingestor.readBatchAsStream(
                "data/transactions.csv",
                transactionList -> {
                    boolean result = sqlRepository.saveTransactions(transactionList);
                    if (!result)
                        IO.println("Result falhou");
                });

        long finalTime = System.currentTimeMillis();
        Optional<Transaction> transaction1 = sqlRepository.getTransactionByOriginName("C1231006815");
        Optional<Transaction> transaction2 = sqlRepository.getTransactionByOriginName("C12345");

        Duration d = Duration.ofMillis(finalTime - initialTime);
        IO.println(String.format("Tempo total: %d min %d s", d.toMinutes(), d.toSecondsPart()));

        transaction1.ifPresentOrElse(
                IO::println,
                () -> IO.println("Transaction from C1231006815 not found.")
        );

        transaction2.ifPresentOrElse(
                IO::println,
                () -> IO.println("Transaction from C12345 not found.")
        );

        IO.println("Total inserts: " + ProgressCounter.get());

    }
}
