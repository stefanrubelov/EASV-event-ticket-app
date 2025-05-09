package easv.ticketapp.dal.db.interaces;

import easv.ticketapp.be.Event;
import easv.ticketapp.be.User;

import java.util.List;

public interface EventRepository extends BaseRepository<Event> {
    List<Event> getAllByUserId(int userId);
    boolean assignCoordinator(Event event, User user);
}
