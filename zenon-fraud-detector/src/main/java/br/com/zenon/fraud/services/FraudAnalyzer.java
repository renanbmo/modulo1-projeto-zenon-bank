package br.com.zenon.fraud.services;

import br.com.zenon.fraud.models.Transaction;
import br.com.zenon.fraud.models.TransactionType;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FraudAnalyzer {

    public List<Transaction> transactions;

    public FraudAnalyzer(List<Transaction> transactions){
        Objects.requireNonNull(transactions);

        this.transactions = transactions;
    }

    public void printSizeOnlyFrauds() {
        long count = fraudStream()
                .count();
        IO.println("Total de Fraudes: " + count);
    }

    public void printHighestValueFrauds(long limit) {
        IO.println("Top 3 Fraudes de Maior Valor:");
        hihgValueFraudsStream()
                .limit(limit)
                .map(t -> t.amount().toPlainString())
                .forEach(IO::println);
    }

    public void printSuspiciousCusomer(long limit) {
        IO.println("Clientes Suspeitos:");

        hihgValueFraudsStream()
                .map(t -> t.origin().name())
                .distinct()
                .limit(limit)
                .forEach(IO::println);
    }

    public void printTotalFraud() {
        var amaunt = fraudStream()
                .map(Transaction::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        IO.println("Prejuízo total: " + amaunt);
    }

    public void printFraudByType(){
        Map<TransactionType, Long> fraudCountByType = fraudStream()
                .collect(
                        Collectors.groupingBy(
                                Transaction::type,
                                Collectors.counting()
                        )
                );

        fraudCountByType.forEach((type, qtd) -> IO.println(type.name() + ": " + qtd));
    }

    private Stream<Transaction> fraudStream(){
        return transactions.stream()
                .filter(Transaction::isFraud);
    }

    private Stream<Transaction> hihgValueFraudsStream(){
        return fraudStream()
                .sorted(Comparator.comparing(Transaction::amount).reversed());
    }
}
