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
                .select("tickets.id", "tickets.event_id", "tickets.price", "tickets.description", "tickets.ticket_type_id, ticket_types.type as ticket_type, events.name as event_name, events.start_date as start_date, events.description as event_description, events.location as event_location")
                .join("ticket_types", "ticket_types.id = tickets.ticket_type_id", "INNER")
                .join("events", "events.id = tickets.event_id", "INNER")
                .where("event_id", "=", eventId)
                .get();

        try {
            while (resultSet.next()) {
                Ticket newTicket = mapModel(resultSet);
                newTicket.setTicketType(new TicketType(resultSet.getString("ticket_type")));
                Event event = new Event(resultSet.getInt("event_id"), resultSet.getString("event_name"), resultSet.getTimestamp("start_date").toLocalDateTime(), resultSet.getString("event_location"), resultSet.getString("event_description"));
                newTicket.setEvent(event);
                tickets.add(newTicket);
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
        Event event = fetchEvent(eventId);
        TicketType ticketType = fetchTicketType(ticketTypeId);

        return new Ticket(id, event, price, description, ticketType);
    }

    private TicketType fetchTicketType(int ticketTypeId) {
        return new TicketType(ticketTypeId);
    }

    private Event fetchEvent(int eventId) {
        return new Event(eventId);
    }

    public void delete(int id) {
        queryBuilder
                .table("tickets")
                .where("id", "=", id)
                .delete();
    }
}
