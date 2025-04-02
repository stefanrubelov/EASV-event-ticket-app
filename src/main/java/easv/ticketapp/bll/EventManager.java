package easv.ticketapp.bll;

import easv.ticketapp.be.Event;
import easv.ticketapp.be.User;
import easv.ticketapp.dal.db.EventRepository;

import java.util.List;
import java.util.logging.Logger;

public class EventManager {
    final private EventRepository eventRepository = new EventRepository();
    final private Logger logger = Logger.getAnonymousLogger();

    public List<Event> getAllEvents() {
        return eventRepository.getAll();
    }

 public List<Event> getAllEventsByUser(User user) {
        return eventRepository.getAllByUserId(user.getId());
    }

    public boolean saveEvent(Event event, User user) {
        Event newEvent = eventRepository.createEvent(event);
        boolean newEventCoordinator = false;
        if (newEvent != null) {

            newEventCoordinator = eventRepository.addCoordinator(newEvent, user);
        }

        if (newEventCoordinator) {
            return true;
        }
        return false;
    }

    public void deleteEvent(Event event) {
        eventRepository.delete(event.getId());
    }
}
