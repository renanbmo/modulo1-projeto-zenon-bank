package br.com.zenon.fraud.repository;

import br.com.zenon.fraud.interfaces.TransactionRepositoryInterface;
import br.com.zenon.fraud.models.Transaction;
import br.com.zenon.fraud.models.TransactionCustomer;
import br.com.zenon.fraud.models.TransactionType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class TransactionSQLRepository implements TransactionRepositoryInterface {
    @Override
    public Optional<Transaction> getTransactionByOriginName(String name) {
        var sql = "SELECT id, step, `type`, amount, name_orig, old_balance_orig, new_balance_orig, name_dest, old_balance_dest, new_balance_dest, is_fraud, is_flagged_fraud\n" +
                "FROM transactions.transactions\n" +
                "WHERE name_orig = ?";

        try(Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setString(1, name);

            try(var rs = ps.executeQuery()){
                if(rs.next()){
                    var step = rs.getInt("step");
                    var type = rs.getString("type");
                    var amount = rs.getBigDecimal("amount");
                    var nameOrig = rs.getString("name_orig");
                    var oldBalanceOrig = rs.getBigDecimal("old_balance_orig");
                    var newBalanceOrig = rs.getBigDecimal("new_balance_orig");
                    var nameDest = rs.getString("name_dest");
                    var oldBalanceDest = rs.getBigDecimal("old_balance_dest");
                    var newBalanceDest = rs.getBigDecimal("new_balance_dest");
                    var isFraud = rs.getBoolean("is_fraud");
                    var isFlaggedFraud = rs.getBoolean("is_flagged_fraud");

                    new Transaction(step, TransactionType.valueOf(type), amount,
                            new TransactionCustomer(nameOrig, oldBalanceOrig, newBalanceOrig),
                            new TransactionCustomer(nameDest, oldBalanceDest, newBalanceDest),
                            isFraud, isFlaggedFraud);
                }
            }

            return Optional.empty();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean saveTransaction(Transaction transaction) {
        var sql = "INSERT INTO transactions.transactions\n" +
                "(step, `type`, amount, name_orig, old_balance_orig, new_balance_orig, name_dest, old_balance_dest, new_balance_dest, is_fraud, is_flagged_fraud)\n" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){

            var i = 1;
            ps.setInt(i++, transaction.step());
            ps.setString(i++, transaction.type().name());
            ps.setBigDecimal(i++, transaction.amount());
            ps.setString(i++, transaction.origin().name());
            ps.setBigDecimal(i++, transaction.origin().oldBalance());
            ps.setBigDecimal(i++, transaction.origin().newBalance());
            ps.setString(i++, transaction.recipient().name());
            ps.setBigDecimal(i++, transaction.recipient().oldBalance());
            ps.setBigDecimal(i++, transaction.recipient().newBalance());
            ps.setBoolean(i++, transaction.isFraud());
            ps.setBoolean(i++, transaction.isFlaggedFraud());

            return ps.executeUpdate() > 0;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/transactions", "root", "senha123");
    };
}
