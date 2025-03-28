package easv.ticketapp.dal.db;

import easv.ticketapp.be.Event;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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

    public void delete(int id) {
        queryBuilder
                .table("events")
                .where("id", "=", id)
                .delete();
    }

    private Event mapModel(ResultSet resultSet, int id) throws SQLException {
        String name = resultSet.getString("name");
        LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
        String location = resultSet.getString("location");
        String description = resultSet.getString("description");



        return new Event(id, name, date, location, description);
    }

    public void updateEvent(Event event) {
        // Use QueryBuilder to construct the update query
        queryBuilder
                .table("events")  // Table to update
                .set("name", event.getName())  // Set the name column
                .set("date", event.getDate())  // Set the date column
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

    public void createEvent(Event event) {
        // Use QueryBuilder to construct the update query
        queryBuilder
                .table("events")  // Table to update
                .insert("name", event.getName())  // Set the name column
                .insert("date", event.getDate())  // Set the date column
                .insert("location", event.getLocation())  // Set the location column
                .insert("description", event.getDescription());  // Set the description column
        // Execute the update
        boolean success = queryBuilder.save();

        if (success) {
            logger.log(Level.INFO, "Event with ID " + event.getId() + " updated successfully.");
        } else {
            logger.log(Level.WARNING, "Failed to update event with ID " + event.getId());
        }
    }


}
