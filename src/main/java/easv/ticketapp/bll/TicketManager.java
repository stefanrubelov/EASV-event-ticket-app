package easv.ticketapp.bll;

import easv.ticketapp.be.ticket.Ticket;
import easv.ticketapp.be.ticket.TicketType;
import easv.ticketapp.dal.db.TicketRepositoryImp;
import easv.ticketapp.dal.db.TicketTypeRepository;

import java.util.List;
import java.util.Optional;

public class TicketManager {

    private final TicketTypeRepository ticketTypeRepository = new TicketTypeRepository();
    private final TicketRepositoryImp ticketRepositoryImp = new TicketRepositoryImp();

    public List<TicketType> getAllTicketTypes() {
        return ticketTypeRepository.getTicketTypes();
    }

    public void addTicket(Ticket newTicket) {
        ticketRepositoryImp.create(newTicket);
    }

    public List<Ticket> getTicketsByEvent(int id) {
        return ticketRepositoryImp.getEventTickets(id);
    }

    public void deleteTicket(Ticket ticket) {
        ticketRepositoryImp.delete(ticket.getId());
    }
}
