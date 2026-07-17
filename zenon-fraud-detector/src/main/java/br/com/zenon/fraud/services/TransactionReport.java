package br.com.zenon.fraud.services;

import br.com.zenon.fraud.mappers.TransactionMapper;
import br.com.zenon.fraud.models.Transaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public class TransactionReport {

    private class ReportStats{
        int totalLines = 0;
        int totalFrauds = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;

        public ReportStats(int totalLines, int totalFrauds, BigDecimal totalAmount) {
            this.totalLines = totalLines;
            this.totalFrauds = totalFrauds;
            this.totalAmount = totalAmount;
        }

        public ReportStats add(ReportStats before, Transaction transaction){
            return new ReportStats(
                    before.totalLines + 1,
                    totalFrauds + (transaction.isFraud() ? 1 : 0),
                    totalAmount.add(transaction.amount())
            );
        }
    }

    public void printReportFile(String filePath) throws IOException {
        var path = Paths.get(filePath);

        ReportStats stats;

        try (var lines = Files.lines(path)) {
            stats = lines.skip(1)
                    .map(TransactionMapper::mapToTransaction)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .reduce(
                            new ReportStats(0,0,BigDecimal.ZERO),
                            (ReportStats rs, Transaction tr) -> rs.add(rs, tr),
                            (s1, s2) -> s1
                    );
        }

        System.out.println("Total de linhas: " + stats.totalLines);
        System.out.println("Total de fraudes: " + stats.totalFrauds);
        System.out.println("Valor total: " + stats.totalAmount.toPlainString());
    }
}
