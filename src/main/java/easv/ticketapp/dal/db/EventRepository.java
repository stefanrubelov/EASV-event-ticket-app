package easv.ticketapp.dal.db;

import easv.ticketapp.be.Event;

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

    private Event mapModel(ResultSet resultSet, int id) throws SQLException {
        String name = resultSet.getString("name");
        java.sql.Date startDate = resultSet.getDate("start_date");
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
        String location = resultSet.getString("location");
        String description = resultSet.getString("description");



        return new Event(id, name, formattedDate, location, description);
    }
}
