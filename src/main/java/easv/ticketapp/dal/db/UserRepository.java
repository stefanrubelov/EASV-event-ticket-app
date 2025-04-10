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

public class UserRepository implements easv.ticketapp.dal.db.interaces.UserRepository {
    private final QueryBuilder queryBuilder = new QueryBuilder();
    final private Logger logger = Logger.getAnonymousLogger();

    @Override
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

    @Override
    public List<User> getAllCoordinators() {
        List<User> users = new ArrayList<>();

        try (ResultSet rs = queryBuilder
                .select("*")
                .from("users")
                .where("user_type", "=", User.COORDINATOR_TYPE)
                .get()) {

            while (rs != null && rs.next()) {
                User user = mapModel(rs);
                users.add(user);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        return users;
    }

    @Override
    public User getById(int id) {
        User user = null;
        ResultSet resultSet = queryBuilder
                .table("users")
                .select("*")
                .where("id", "=", id)
                .get();

        try {
            while (resultSet.next()) {
                user = mapModel(resultSet);
                user.setUserType(resultSet.getInt("user_type"));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();

        try (ResultSet rs = queryBuilder
                .select("*")
                .from("users")
                .get()) {

            while (rs != null && rs.next()) {
                User user = mapModel(rs);
                users.add(user);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        return users;
    }

    @Override
    public void delete(int id) {
        queryBuilder
                .table("users")
                .where("id", "=", id)
                .delete();
    }

    @Override
    public void update(User entity) {
        try {
            queryBuilder
                    .table("users")
                    .set("name", entity.getName())
                    .set("email", entity.getEmail())
                    .where("id", "=", entity.getId())
                    .update();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @Override
    public User create(User user) {
        User newCoordinator = null;
        ResultSet resultSet = queryBuilder.table("users")
                .insert("name", user.getName())
                .insert("email", user.getEmail())
                .insert("password", user.getPassword())
                .insert("user_type", user.getUserType())
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

    @Override
    public boolean updatePassword(User user, String password) {
        return queryBuilder.table("users")
                .set("password", password)
                .where("email", "=", user.getEmail())
                .update();
    }

    @Override
    public boolean addCoordinator(Event event, User user) {
        try {
            queryBuilder.table("event_user")
                    .insert("event_id", event.getId())
                    .insert("user_id", user.getId())
                    .save();

            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return false;
        }
    }

    private User mapModel(ResultSet rs) throws SQLException {
        String name = rs.getString("name");
        String email = rs.getString("email");
        String password = rs.getString("password");
        int userType = rs.getInt("user_type");
        Timestamp createdAt = rs.getTimestamp("created_at");
        Timestamp updatedAt = rs.getTimestamp("updated_at");

        return new User(name, email, password, userType, createdAt.toLocalDateTime(), updatedAt.toLocalDateTime());
    }

}

