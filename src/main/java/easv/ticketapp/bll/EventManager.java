package easv.ticketapp.bll;

import easv.ticketapp.be.Event;
import easv.ticketapp.be.User;
import easv.ticketapp.dal.db.EventRepositoryImp;

import java.util.List;
import java.util.logging.Logger;

public class EventManager {
    final private EventRepositoryImp eventRepositoryImp = new EventRepositoryImp();
    final private Logger logger = Logger.getAnonymousLogger();

    public List<Event> getAllEvents() {
        return eventRepositoryImp.getAll();
    }

 public List<Event> getAllEventsByUser(User user) {
        return eventRepositoryImp.getAllByUserId(user.getId());
    }

    public boolean saveEvent(Event event, User user) {
        Event newEvent = eventRepositoryImp.create(event);
        boolean newEventCoordinator = false;
        if (newEvent != null) {

            newEventCoordinator = eventRepositoryImp.addCoordinator(newEvent, user);
        }

        if (newEventCoordinator) {
            return true;
        }
        return false;
    }

    public void deleteEvent(Event event) {
        eventRepositoryImp.delete(event.getId());
    }
}
