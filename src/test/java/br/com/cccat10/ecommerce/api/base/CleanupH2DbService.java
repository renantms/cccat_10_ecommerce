package br.com.cccat10.ecommerce.api.base;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

@Component
public class CleanupH2DbService {
    public static final String H2_DB_PRODUCT_NAME = "H2";

    private final DataSource dataSource;

    public CleanupH2DbService(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void cleanup(String schemaName) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            if (isH2Database(connection)) {
                disableConstraints(statement);
                truncateTables(statement, schemaName);
                resetSequences(statement, schemaName);
                enableConstraints(statement);
            }
        }
    }
    private void resetSequences(Statement statement, String schemaName) throws SQLException {
        getSchemaSequences(statement, schemaName).forEach(sequenceName ->
        {
            try {
                executeStatement(statement, String.format("ALTER SEQUENCE %s RESTART WITH 1", sequenceName));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }
    private void truncateTables(Statement statement, String schemaName) throws SQLException {
        getSchemaTables(statement, schemaName)
                .forEach(tableName -> {
                    try {
                        executeStatement(statement, "TRUNCATE TABLE \"" + tableName + "\"");
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                });
    }
    private void enableConstraints(Statement statement) throws SQLException {
        executeStatement(statement, "SET REFERENTIAL_INTEGRITY TRUE");
    }
    private void disableConstraints(Statement statement) throws SQLException {
        executeStatement(statement, "SET REFERENTIAL_INTEGRITY FALSE");
    }

    private boolean isH2Database(Connection connection) throws SQLException {
        return H2_DB_PRODUCT_NAME.equals(connection.getMetaData().getDatabaseProductName());
    }

    private void executeStatement(Statement statement, String sql) throws SQLException {
        statement.executeUpdate(sql);
    }

    private Set<String> getSchemaTables(Statement statement, String schemaName) throws SQLException {
        String sql = String.format("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES  where TABLE_SCHEMA='%s'", schemaName);
        return queryForList(statement, sql);
    }

    private Set<String> getSchemaSequences(Statement statement, String schemaName) throws SQLException {
        String sql = String.format("SELECT SEQUENCE_NAME FROM INFORMATION_SCHEMA.SEQUENCES WHERE SEQUENCE_SCHEMA='%s'", schemaName);
        return queryForList(statement, sql);
    }

    private Set<String> queryForList(Statement statement, String sql) throws SQLException {
        Set<String> tables = new HashSet<>();
        try (ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                tables.add(rs.getString(1));
            }
        }
        return tables;
    }
}