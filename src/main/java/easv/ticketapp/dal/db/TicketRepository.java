package easv.ticketapp.dal.db;

import easv.ticketapp.be.ticket.Ticket;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TicketRepository {
    final private QueryBuilder queryBuilder = new QueryBuilder();
    final private Logger logger = Logger.getAnonymousLogger();

    public Ticket create(Ticket ticket) {
        Ticket newTicket = null;
        ResultSet resultSet = queryBuilder.table("tickets")
                .insert("event_id", ticket.getEvent().getId())
                .insert("price", ticket.getPrice())
                .insert("description", ticket.getDescription())
                .insert("ticket_type_id", ticket.getTicketType().getId())
                .saveAndReturn();

        try {
            while (resultSet.next()) {
                newTicket = new Ticket(resultSet.getInt("id"));

                return newTicket;
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return newTicket;
    }



    /*private Ticket mapModel(ResultSet rs, int id) {
        String eventName = rs.getString("eventName");
        LocalDateTime price = rs.getTimestamp("price").toLocalDateTime();
        LocalDateTime seat_number = rs.getTimestamp("seat_number").toLocalDateTime();
        String perks = rs.getString("perks");

        return new Ticket(id, title, path, createdAt, updatedAt);
}
     */
}
