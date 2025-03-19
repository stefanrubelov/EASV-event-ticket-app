package easv.ticketapp.dal.db;

import easv.ticketapp.be.Event;
import easv.ticketapp.be.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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

    public List<User> getAllCoordinators(){
        List<User> users = new ArrayList<>();

        try (ResultSet rs = queryBuilder
                .select("*")
                .from("users")
                .where("user_type", "=",User.COORDINATOR_TYPE)
                .get()) {

            while (rs != null && rs.next()) {
                User user = mapModel(rs, rs.getInt("id"));
                users.add(user);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        return users;
    }

    public void delete(int id) {
        queryBuilder
                .table("users")
                .where("id", "=", id)
                .delete();
    }

    private User mapModel(ResultSet rs, int id) throws SQLException {
        String name = rs.getString("name");
        String email = rs.getString("email");
        String password = rs.getString("password");
        int userType = rs.getInt("user_type");
        Timestamp createdAt = rs.getTimestamp("created_at");
        Timestamp updatedAt = rs.getTimestamp("updated_at");

        return new User(id,name,email,password,userType, createdAt.toLocalDateTime(), updatedAt.toLocalDateTime());
    }

    public User create(User user) {
        User newCoordinator = null;
        ResultSet resultSet = queryBuilder.table("users")
                .insert("name", user.getName())
                .insert("email", user.getEmail())
                .insert("password", user.getPassword())
                //.insert("user_type", user.getUserType())
                .saveAndReturn();

        try {
            while (resultSet.next()) {
                newCoordinator = new User(resultSet.getInt("id"));

                return newCoordinator;
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return newCoordinator;
    }
}
