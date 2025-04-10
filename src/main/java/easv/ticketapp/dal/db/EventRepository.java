package easv.ticketapp.dal.db;

import easv.ticketapp.be.Event;
import easv.ticketapp.be.User;
import easv.ticketapp.dal.db.connection.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventRepository implements easv.ticketapp.dal.db.interaces.EventRepository {
    final private QueryBuilder queryBuilder = new QueryBuilder();
    final private Logger logger = Logger.getAnonymousLogger();

    @Override
    public Event getById(int id) {
        Event event = null;
        ResultSet resultSet = queryBuilder
                .from("events")
                .select("*")
                .where("id", "=", id)
                .get();

        try {
            while (resultSet.next()) {
                event = mapModel(resultSet, resultSet.getInt("id"));
            }

            return event;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);

            return event;
        }
    }

    @Override
    public List<Event> getAll() {
        List<Event> events = new ArrayList<>();

        try (ResultSet rs = queryBuilder
                .select("*")
                .from("events")
                .get()) {

            while (rs != null && rs.next()) {
                Event event = mapModel(rs, rs.getInt("id"));
                events.add(event);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        return events;
    }

    @Override
    public List<Event> getAllByUserId(int userId) {
        List<Event> events = new ArrayList<>();

        try (ResultSet rs = queryBuilder
                .select("events.id as id, events.name as name, events.location as location, events.description as description, events.start_date as start_date, event_user.id as u_e_id")
                .from("events")
                .join("event_user", "events.id = event_user.event_id", "INNER")
                .where("event_user.user_id", "=", userId)
                .get()) {

            while (rs != null && rs.next()) {
                Event event = mapModel(rs, rs.getInt("id"));
                events.add(event);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        return events;
    }

    @Override
    public void delete(int id) {
        queryBuilder
                .table("events")
                .where("id", "=", id)
                .delete();
    }

    @Override
    public void update(Event event) {
        // Use QueryBuilder to construct the update query
            queryBuilder
                .table("events")
                .set("name", event.getName())
                .set("start_date", event.getDate())
                .set("location", event.getLocation())
                .set("description", event.getDescription())
                .where("id", "=", event.getId());

        // Execute the update
        boolean success = queryBuilder.update();

        if (success) {
            logger.log(Level.INFO, "Event with ID " + event.getId() + " updated successfully.");
        } else {
            logger.log(Level.WARNING, "Failed to update event with ID " + event.getId());
        }
    }

    @Override
    public Event create(Event event) {
        ResultSet resultSet = queryBuilder
                .table("events")
                .insert("name", event.getName())
                .insert("start_date", event.getDate())
                .insert("location", event.getLocation())
                .insert("description", event.getDescription())
                .saveAndReturn();
        try {
            while (resultSet.next()) {
                return new Event(resultSet.getInt("id"));
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public boolean assignCoordinator(Event event, User user) {
        return queryBuilder
                .table("event_user")
                .insert("event_id", event.getId())
                .insert("user_id", user.getId())
                .save();
    }

    public boolean isUserAssignedToEvent(Event event, User user) {
        String sql = "SELECT COUNT(*) AS count FROM event_user WHERE event_id = ? AND user_id = ?";

        DatabaseConnection databaseConnection = new DatabaseConnection();

        try (Connection conn = databaseConnection.getConnection();  // however you get connection
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, event.getId());
            stmt.setInt(2, user.getId());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt("count");
                System.out.println("Assignment count: " + count);
                return count > 0;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        return false;
    }

    public boolean addCoordinator(Event event, User user) {
        return queryBuilder
                .table("event_user")
                .insert("event_id", event.getId())
                .insert("user_id", user.getId())
                .save();
    }

    private Event mapModel(ResultSet resultSet, int id) throws SQLException {
        String name = resultSet.getString("name");
        LocalDateTime date = resultSet.getTimestamp("start_date").toLocalDateTime();
        String location = resultSet.getString("location");
        String description = resultSet.getString("description");


        return new Event(id, name, date, location, description);
    }
}
