package br.com.zenon.fraud;

import br.com.zenon.fraud.interfaces.TransactionRepositoryInterface;
import br.com.zenon.fraud.models.Transaction;
import br.com.zenon.fraud.services.*;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Main {
    public static void main(String[] args) throws Exception {
        task07();
    }

    private static void task05() throws IOException {
        TransactionIngestor transactionIngestor = new TransactionIngestor();
        List<Transaction> transactions = transactionIngestor.getTransactions("data/transactions.csv", 50_000L);

        FraudAnalyzer analyzer = new FraudAnalyzer(transactions);

        analyzer.printSizeOnlyFrauds();
        analyzer.printHighestValueFrauds(3);
        analyzer.printSuspiciousCusomer(5);
        analyzer.printTotalFraud();
        analyzer.printFraudByType();
    }

    private static void task06() throws IOException {
        TransactionIngestor transactionIngestor = new TransactionIngestor();
        List<Transaction> transactions = transactionIngestor.getTransactions("data/transactions.csv", 100_000L);

        TransactionRepositoryInterface repositoryList = new TransactionListRepository(transactions);
        TransactionRepositoryInterface repositoryMap = new TransactionMapRepository(transactions);

        getAndPrintTransactions(repositoryList, "C12345");
        getAndPrintTransactions(repositoryList, "C1231006815");

        IO.println("---------------List Benchmark-------------------");
        getAndPrintBenchmark(repositoryList, "C1868032458");

        IO.println();
        IO.println();

        IO.println("---------------List Benchmark-------------------");
        getAndPrintBenchmark(repositoryMap, "C186803245a8");

    }

    private static void getAndPrintTransactions(TransactionRepositoryInterface repository, String name){
        repository.getTransactionByOriginName(name)
                .ifPresentOrElse(
                        IO::println,
                        () -> IO.println("Transação não encontrada para o cliente " + name)
                );
    }

    private static void getAndPrintBenchmark(TransactionRepositoryInterface repository, String name){

        long initialTime = System.nanoTime();
        getAndPrintTransactions(repository, name);
        long finalTime = System.nanoTime();

        IO.println("Total time: " + (finalTime - initialTime) + "ns");
    }

    private static void task07() throws IOException {
        TransactionReport report = new TransactionReport();

        IO.println("################## pt-BR ##################");
        report.printReportFile("data/transactions.csv", Locale.of("pt", "BR"));

        IO.println();
        IO.println();

        IO.println("################## US ##################");
        report.printReportFile("data/transactions.csv", Locale.US);
    }
}
