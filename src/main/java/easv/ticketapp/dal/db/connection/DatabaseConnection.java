package easv.ticketapp.dal.db.connection;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import easv.ticketapp.utils.Env;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection implements AutoCloseable {
    private static DatabaseConnection INSTANCE;

    public Connection getConnection() throws SQLServerException {
        SQLServerDataSource ds;
        ds = new SQLServerDataSource();
        ds.setDatabaseName(Env.get("DB_NAME"));
        ds.setUser(Env.get("DB_USER"));
        ds.setPassword(Env.get("DB_PASSWORD"));
        ds.setServerName(Env.get("DB_HOST"));
        ds.setPortNumber(Integer.parseInt(Env.get("DB_PORT", "1433")));
        ds.setTrustServerCertificate(true);
        return ds.getConnection();
    }

    public boolean testConnection() throws SQLException {
        try (Connection conn = getConnection()) {
            return true;
        }
    }

    public static DatabaseConnection getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DatabaseConnection();
        }
        return INSTANCE;
    }


    @Override
    public void close() throws Exception {

    }
}