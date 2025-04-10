package easv.ticketapp.dal.db.interaces;

import easv.ticketapp.be.Event;
import easv.ticketapp.be.ticket.Ticket;
import easv.ticketapp.be.ticket.TicketType;

import java.util.List;

public interface TicketRepository extends BaseRepository<Ticket> {
    List<Ticket> getEventTickets(int eventId);

    default TicketType fetchTicketType(int ticketTypeId) {
        return new TicketType(ticketTypeId);
    }

    default Event fetchEvent(int eventId) {
        return new Event(eventId);
    }
}
