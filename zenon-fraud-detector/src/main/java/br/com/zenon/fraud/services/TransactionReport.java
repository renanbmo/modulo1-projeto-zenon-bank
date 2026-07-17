package br.com.zenon.fraud.services;

import br.com.zenon.fraud.mappers.TransactionMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TransactionReport {

    public void printReportFile(String filePath) throws IOException {
        var path = Paths.get(filePath);

        class ReportStats{
            int totalLines = 0;
            int totalFrauds = 0;
            BigDecimal totalAmount = BigDecimal.ZERO;
        }

        var stats = new ReportStats();

        try (var lines = Files.lines(path)) {
            lines.forEach(line -> {
                var transaction = TransactionMapper.mapToTransaction(line);
                if (transaction.isPresent()) {
                    stats.totalLines++;
                    var t = transaction.get();
                    if (t.isFraud()) {
                        stats.totalFrauds++;
                    }
                    stats.totalAmount = stats.totalAmount.add(t.amount());
                }
            });
        }

        System.out.println("Total de linhas: " + stats.totalLines);
        System.out.println("Total de fraudes: " + stats.totalFrauds);
        System.out.println("Valor total: " + stats.totalAmount.toPlainString());
    }
}
