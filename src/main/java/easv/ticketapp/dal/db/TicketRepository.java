package easv.ticketapp.dal.db;

import easv.ticketapp.be.Event;
import easv.ticketapp.be.ticket.Ticket;
import easv.ticketapp.be.ticket.TicketType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TicketRepository {
    final private QueryBuilder queryBuilder = new QueryBuilder();
    final private Logger logger = Logger.getAnonymousLogger();

    public Ticket createTicket(Ticket ticket) {
        Ticket newTicket = null;
        ResultSet resultSet = queryBuilder.table("tickets")
                .insert("eventName", ticket.getEventName())
                .insert("price", ticket.getPrice())
                .insert("perks", ticket.getPerks())
                .insert("description", ticket.getDescription())
                .insert("location", ticket.getLocation())
                .insert("date", ticket.getDate())
                .insert("ticketType", ticket.getTicketType())
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

     */
}
