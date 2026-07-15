package br.com.zenon.fraud;

import br.com.zenon.fraud.models.Transaction;
import br.com.zenon.fraud.services.FraudAnalyzer;
import br.com.zenon.fraud.services.TransactionIngestor;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        TransactionIngestor transactionIngestor = new TransactionIngestor();
        List<Transaction> transactions = transactionIngestor.getTransactions("data/transactions.csv", 50_000L);

        FraudAnalyzer analyzer = new FraudAnalyzer(transactions);

        analyzer.printSizeOnlyFrauds();
        analyzer.printHighestValueFrauds(3);
        analyzer.printSuspiciousCusomer(5);
        analyzer.printTotalFraud();
        analyzer.printFraudByType();
    }
}
