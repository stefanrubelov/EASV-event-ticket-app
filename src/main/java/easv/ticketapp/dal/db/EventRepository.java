package easv.ticketapp.dal.db;

import easv.ticketapp.be.Event;
import easv.ticketapp.be.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventRepository {
    final private QueryBuilder queryBuilder = new QueryBuilder();
    final private Logger logger = Logger.getAnonymousLogger();

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

    public void delete(int id) {
        queryBuilder
                .table("events")
                .where("id", "=", id)
                .delete();
    }

    private Event mapModel(ResultSet resultSet, int id) throws SQLException {
        String name = resultSet.getString("name");
        LocalDateTime date = resultSet.getTimestamp("start_date").toLocalDateTime();
        String location = resultSet.getString("location");
        String description = resultSet.getString("description");


        return new Event(id, name, date, location, description);
    }

    public void updateEvent(Event event) {
        // Use QueryBuilder to construct the update query
        queryBuilder
                .table("events")  // Table to update
                .set("name", event.getName())  // Set the name column
                .set("start_date", event.getDate())  // Set the date column
                .set("location", event.getLocation())  // Set the location column
                .set("description", event.getDescription())  // Set the description column
                .where("id", "=", event.getId());  // Only update where the event ID matches

        // Execute the update
        boolean success = queryBuilder.update();

        if (success) {
            logger.log(Level.INFO, "Event with ID " + event.getId() + " updated successfully.");
        } else {
            logger.log(Level.WARNING, "Failed to update event with ID " + event.getId());
        }
    }

    public Event createEvent(Event event) {
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

    public boolean addCoordinator(Event event, User user) {
        return queryBuilder
                .table("event_user")
                .insert("event_id", event.getId())
                .insert("user_id", user.getId())
                .save();
    }

    public List<Event> getAllEventNames() {
        List<Event> events = new ArrayList<>();

        try (ResultSet rs = queryBuilder
                .select("id", "name", "start_date")
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
}