package site.easy.to.build.crm.importcsv.service;


import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DatabaseResetService {
    private final DataSource dataSource;
    private final EntityManager manager;

    @Autowired
    public DatabaseResetService(DataSource dataSource, EntityManager manager) {
        this.dataSource = dataSource;
        this.manager = manager;
    }

    public Set<String> getDbTablesName() throws SQLException {
        Set<EntityType<?>> entities = manager.getMetamodel().getEntities();
        return entities.stream().map(e -> e.getName()).collect(Collectors.toSet());
/*        Connection connection = null;
        Set<String> tables = new HashSet<>();
        try {
            connection = this.dataSource.getConnection();
            if (connection == null)
                throw new SQLException("Failed to get connection");

            String query = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'";

            try(Statement stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery(query))
            {
                while (rs.next()){
                    tables.add(rs.getString("table_name"));
                }
            }
            return tables;
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();  // Rollback in case of failure
            }
            throw new SQLException("Error resetting data", e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }*/
    }
    public boolean resetData() throws SQLException {
        Connection connection = null;
        Statement stmt = null;
        try {
            connection = dataSource.getConnection();
            if (connection == null)
                throw new SQLException("Failed to get connection");

            connection.setAutoCommit(false);

            // Disable foreign key checks
            String disableFKChecks = "SET FOREIGN_KEY_CHECKS = 0;";

            stmt = connection.createStatement();
            stmt.executeUpdate(disableFKChecks);  // Disable foreign key checks

            this.deleteAllData(stmt);
            this.resetAutoIncrment(stmt);

            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 1;");  // Re-enable foreign key checks

            connection.commit();  // Commit the transaction
            return true;
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();  // Rollback in case of failure
            }
            throw new SQLException("Error resetting data", e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    private boolean deleteAllData(Statement stmt) throws SQLException {
        // Queries to delete data
        String[] deleteQueries = {
                "DELETE FROM contract_settings",
                "DELETE FROM email_template",
                "DELETE FROM file",
                "DELETE FROM google_drive_file",
                "DELETE FROM lead_action",
                "DELETE FROM lead_settings",
                "DELETE FROM ticket_settings",
                "DELETE FROM trigger_contract",
                "DELETE FROM trigger_lead",
                "DELETE FROM trigger_ticket",
                "DELETE FROM customer",
                "DELETE FROM customer_login_info"
        };

        // Execute DELETE queries
        for (String query : deleteQueries) {
            stmt.executeUpdate(query);
        }

        return true;
    }
    private boolean resetAutoIncrment(Statement stmt) throws SQLException {
        // Reset AUTO_INCREMENT for all tables
        String[] resetAutoIncrementQueries = {
                "ALTER TABLE contract_settings AUTO_INCREMENT = 1;",
                "ALTER TABLE email_template AUTO_INCREMENT = 1;",
                "ALTER TABLE file AUTO_INCREMENT = 1;",
                "ALTER TABLE google_drive_file AUTO_INCREMENT = 1;",
                "ALTER TABLE lead_action AUTO_INCREMENT = 1;",
                "ALTER TABLE lead_settings AUTO_INCREMENT = 1;",
                "ALTER TABLE ticket_settings AUTO_INCREMENT = 1;",
                "ALTER TABLE trigger_contract AUTO_INCREMENT = 1;",
                "ALTER TABLE trigger_lead AUTO_INCREMENT = 1;",
                "ALTER TABLE trigger_ticket AUTO_INCREMENT = 1;",
                "ALTER TABLE customer AUTO_INCREMENT = 1;",
                "ALTER TABLE customer_login_info AUTO_INCREMENT = 1;"
        };

        for (String query : resetAutoIncrementQueries) {
            stmt.executeUpdate(query);
        }
        return true;
    }
}
