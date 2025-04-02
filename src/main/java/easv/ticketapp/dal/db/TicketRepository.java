package easv.ticketapp.dal.db;

import easv.ticketapp.be.Event;
import easv.ticketapp.be.ticket.Ticket;
import easv.ticketapp.be.ticket.TicketType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

    public List<Ticket> getEventTickets(int eventId) {
        List<Ticket> tickets = new ArrayList<>();
        ResultSet resultSet = queryBuilder
                .table("tickets")
                .select("id", "event_id", "price", "description", "ticket_type_id")
                //.where("event_id", eventId)
                .get();

        try {
            while (resultSet.next()) {
                tickets.add(mapModel(resultSet));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        return tickets;
    }

    private Ticket mapModel(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int eventId = rs.getInt("event_id");
        double price = rs.getDouble("price");
        String description = rs.getString("description");
        int ticketTypeId = rs.getInt("ticket_type_id");

        Event event = fetchEvent(eventId);  // Implementa este método para obter o evento da BD
        TicketType ticketType = fetchTicketType(ticketTypeId); // Implementa este método

        return new Ticket(id, event, price, description, ticketType);
    }

    private TicketType fetchTicketType(int ticketTypeId) {
        return new TicketType(ticketTypeId);
    }

    private Event fetchEvent(int eventId) {
        return new Event(eventId);
    }
}
