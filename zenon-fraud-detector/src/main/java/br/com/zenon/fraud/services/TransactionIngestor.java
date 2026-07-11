package br.com.zenon.fraud.services;

import br.com.zenon.fraud.models.Transaction;
import br.com.zenon.fraud.models.TransactionCustomer;
import br.com.zenon.fraud.models.TransactionType;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    private Optional<Transaction> mapToTransaction(String line){
        String[] fields = line.split(",");

        try{
            if(fields.length != 11)
                throw new IllegalArgumentException("Invalid number of columns");

            Arrays.stream(fields)
                    .forEach(field -> {
                        if(field.isBlank())
                            throw new IllegalArgumentException("Field cannot be blank");
                    });

            TransactionCustomer customerOrig = new TransactionCustomer(
                    parseName(fields[NAME_ORIG_INDEX]),
                    parseDecimal(fields[OLD_BALANCE_ORIG_INDEX], "oldBalanceOrig"),
                    parseDecimal(fields[NEW_BALANCE_ORIG_INDEX], "newBalanceOrig")
            );

            TransactionCustomer customerDest = new TransactionCustomer(
                    parseName(fields[NAME_DEST_INDEX]),
                    parseDecimal(fields[OLD_BALANCE_DEST_INDEX], "oldBalanceDest"),
                    parseDecimal(fields[NEW_BALANCE_DEST_INDEX], "newBalanceDest")
            );

            return Optional.of(new Transaction(
                    parseStep(fields[STEP_INDEX]),
                    TransactionType.valueOf(fields[TYPE_INDEX]),
                    parseDecimal(fields[AMOUNT_INDEX],"Amount"),
                    customerOrig,
                    customerDest,
                    fields[IS_FRAUD_INDEX].equals("1"),
                    fields[IS_FLAGGED_FRAUD_INDEX].equals("1")
            ));
        } catch (Exception e) {
            String sb = "Erro: " +
                    line +
                    " | " +
                    e.getClass().getName() +
                    ": " +
                    e.getMessage();

            System.err.println(sb);

            return Optional.empty();
        }
    }

    private int parseStep(String step) {
        var intStep = Integer.parseInt(step);

        if (intStep <= 0)
            throw new IllegalArgumentException("Step must be positive: " + step);

        return intStep;
    }

    private String parseName(String name){
        if (name.isBlank())
            throw new IllegalArgumentException("Name cannot be blank: " + name);

        return name.trim();
    }

    private TransactionType parseType(String type){
        return TransactionType.valueOf(type);
    }

    private BigDecimal parseDecimal(String value, String fieldName) {
        var bigDecimal = new BigDecimal(value);

        if (bigDecimal.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException(fieldName + " should be positive " + value);

        return bigDecimal;
    }
}
