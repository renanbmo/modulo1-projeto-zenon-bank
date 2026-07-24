package br.com.zenon.fraud;

import br.com.zenon.fraud.models.Transaction;
import br.com.zenon.fraud.repository.TransactionSQLRepository;
import br.com.zenon.fraud.services.TransactionIngestor;

import java.io.IOException;
import java.util.Optional;

public class DBMain{
    void main() throws IOException {
        var ingestor = new TransactionIngestor();
//        var transactions = ingestor.getTransactions("data/transactions.csv", 10_000);
        var sqlRepository = new TransactionSQLRepository();

//        long initialTime = System.currentTimeMillis();
//
//        transactions.forEach(sqlRepository::saveTransaction);
//
//        long finalTime = System.currentTimeMillis();

        Optional<Transaction> transaction1 = sqlRepository.getTransactionByOriginName("C1231006815");
        Optional<Transaction> transaction2 = sqlRepository.getTransactionByOriginName("C12345");

//        IO.println("Ingestion total time: " + (finalTime - initialTime) + "ms");

        transaction1.ifPresentOrElse(
                IO::println,
                () -> IO.println("Transaction from C1231006815 not found.")
        );

        transaction2.ifPresentOrElse(
                IO::println,
                () -> IO.println("Transaction from C12345 not found.")
        );

    }
}
