package br.com.zenon.fraud;

import br.com.zenon.fraud.models.Transaction;
import br.com.zenon.fraud.services.TransactionIngestor;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        var tIni = System.currentTimeMillis();
        TransactionIngestor transactionIngestor = new TransactionIngestor();
        List<Transaction> transactions = transactionIngestor.getTransactions("data/transactions_with_bad_data.cvs");
        var tFin = System.currentTimeMillis();

        IO.println( "Time to ingest transactions: " + (tFin - tIni) + "ms");

        transactions.stream()
                .limit(10)
                .forEach(IO::println);

        IO.println("total transactions: " + transactions.size());
    }
}
