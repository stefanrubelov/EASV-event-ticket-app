package easv.ticketapp.dal.db;

import easv.ticketapp.be.Event;
import easv.ticketapp.be.User;

import java.util.List;

public interface EventRepository extends BaseRepository<Event> {
    List<Event> getAllByUserId(int userId);
    boolean addCoordinator(Event event, User user);
}
