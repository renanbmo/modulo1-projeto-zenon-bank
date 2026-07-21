package br.com.zenon.fraud.services;

import br.com.zenon.fraud.mappers.TransactionMapper;
import br.com.zenon.fraud.models.Transaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class TransactionReport {

    private record ReportStats(int totalLines, int totalFrauds, BigDecimal totalAmount){

        public static final ReportStats ZERO = new ReportStats(0,0,BigDecimal.ZERO);

        public ReportStats add(Transaction transaction){
            return new ReportStats(
                    totalLines + 1,
                    totalFrauds + (transaction.isFraud() ? 1 : 0),
                    totalAmount.add(transaction.amount())
            );
        }
    }

    public void printReportFile(String filePath, Locale locale) throws IOException {
        var path = Paths.get(filePath);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
        currencyFormat.setCurrency(Currency.getInstance("USD"));
        NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
        ResourceBundle bundle = ResourceBundle.getBundle("report", locale);

        ReportStats stats;

        try (var lines = Files.lines(path)) {
            stats = lines.skip(1)
                    .map(TransactionMapper::mapToTransaction)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .reduce(
                            ReportStats.ZERO,
                            ReportStats::add,
                            (s1, s2) -> s1
                    );
        }

        System.out.println(bundle.getString("total.lines") + numberFormat.format(stats.totalLines));
        System.out.println(bundle.getString("total.frauds") + numberFormat.format(stats.totalFrauds));
        System.out.println(bundle.getString("total.value") + currencyFormat.format(stats.totalAmount));
    }
}
