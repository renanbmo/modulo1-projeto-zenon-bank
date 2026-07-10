package br.com.zenon.fraud.services;

import br.com.zenon.fraud.models.Transaction;
import br.com.zenon.fraud.models.TransactionCustomer;
import br.com.zenon.fraud.models.TransactionType;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionIngestor {
    public static final int STEP_INDEX = 0;
    public static final int TYPE_INDEX = 1;
    public static final int AMOUNT_INDEX = 2;
    public static final int NAME_ORIG_INDEX = 3;
    public static final int OLD_BALANCE_ORIG_INDEX = 4;
    public static final int NEW_BALANCE_ORIG_INDEX = 5;
    public static final int NAME_DEST_INDEX = 6;
    public static final int OLD_BALANCE_DEST_INDEX = 7;
    public static final int NEW_BALANCE_DEST_INDEX = 8;
    public static final int IS_FRAUD_INDEX = 9;
    public static final int IS_FLAGGED_FRAUD_INDEX = 10;

    public List<Transaction> getTransactions(String filePath) throws IOException {
        var path = Paths.get(filePath);

        List<String> allLines = Files.readAllLines(path);

        return allLines.stream()
                .skip(1) // skip header
                .limit(1000)
                .map(this::mapToTransaction)
                .toList();
    }

    private Transaction mapToTransaction(String line){
        String[] fields = line.split(",");

        TransactionCustomer customerOrig = new TransactionCustomer(
                fields[NAME_ORIG_INDEX],
                new BigDecimal(fields[OLD_BALANCE_ORIG_INDEX]),
                new BigDecimal(fields[NEW_BALANCE_ORIG_INDEX])
        );

        TransactionCustomer customerDest = new TransactionCustomer(
                fields[NAME_DEST_INDEX],
                new BigDecimal(fields[OLD_BALANCE_DEST_INDEX]),
                new BigDecimal(fields[NEW_BALANCE_DEST_INDEX])
        );

        return new Transaction(
                Integer.parseInt(fields[STEP_INDEX]),
                TransactionType.valueOf(fields[TYPE_INDEX]),
                new BigDecimal(fields[AMOUNT_INDEX]),
                customerOrig,
                customerDest,
                fields[IS_FRAUD_INDEX].equals("1"),
                fields[IS_FLAGGED_FRAUD_INDEX].equals("1")
                );
    }
}
