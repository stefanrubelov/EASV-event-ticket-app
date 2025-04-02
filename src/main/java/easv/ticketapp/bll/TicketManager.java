package easv.ticketapp.bll;

import easv.ticketapp.be.ticket.Ticket;
import easv.ticketapp.be.ticket.TicketType;
import easv.ticketapp.dal.db.TicketRepository;
import easv.ticketapp.dal.db.TicketTypeRepository;

import java.util.List;

public class TicketManager {

    private final TicketTypeRepository ticketTypeRepository = new TicketTypeRepository();
    private final TicketRepository ticketRepository = new TicketRepository();

    public List<TicketType> getAllTicketTypes() {

        return ticketTypeRepository.getTicketTypes();
    }

    public void addTicket(Ticket newTicket) {
        ticketRepository.createTicket(newTicket);
    }

    public void addTicketType(TicketType newTicketType) {
        ticketTypeRepository.addTicketType(newTicketType);
    }
}
