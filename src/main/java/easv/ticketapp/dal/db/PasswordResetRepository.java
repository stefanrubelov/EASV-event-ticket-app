package easv.ticketapp.dal.db;

import easv.ticketapp.be.PasswordReset;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class PasswordResetRepository {
    final private QueryBuilder queryBuilder = new QueryBuilder();
    final private Logger logger = Logger.getAnonymousLogger();
    private final static String TABLE_NAME = "password_resets";

    public PasswordReset findToken(String token) {
        ResultSet result = queryBuilder
                .select("*")
                .from(TABLE_NAME)
                .where("token", "=", token)
                .get();
        try {
            if (result.next()) {
                return new PasswordReset(result.getInt("user_id"), result.getString("token"), result.getTimestamp("created_at").toLocalDateTime());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean createToken(PasswordReset passwordReset) {
        return queryBuilder.table(TABLE_NAME)
                .insert("user_id", passwordReset.getUserId())
                .insert("token", passwordReset.getToken())
                .insert("created_at", passwordReset.getCreatedAt())
                .save();
    }

    public boolean deleteTokens(int user_id) {
        return queryBuilder.from(TABLE_NAME)
                .where("user_id", "=", user_id)
                .delete();
    }
}
