package easv.ticketapp.dal.db;

import easv.ticketapp.be.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserRepository {

    private final QueryBuilder queryBuilder = new QueryBuilder();
    final private Logger logger = Logger.getAnonymousLogger();

    public User findByEmail(String email) {
        User user = null;
        ResultSet result = queryBuilder.from("users")
                .where("email", "=", email)
                .orWhere("email", "like", email + "%")
                .get();

        try {
            if (result.next()) {
                user = new User(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("email"),
                        result.getString("password"),
                        result.getInt("user_type"),
                        result.getTimestamp("created_at").toLocalDateTime(),
                        result.getTimestamp("updated_at").toLocalDateTime()
                );
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return user;
    }
}
