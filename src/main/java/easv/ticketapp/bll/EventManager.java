package easv.ticketapp.bll;

import easv.ticketapp.be.Event;
import easv.ticketapp.dal.db.EventRepository;

import java.util.List;
import java.util.logging.Logger;

public class EventManager {
    final private EventRepository eventRepository = new EventRepository();
    final private Logger logger = Logger.getAnonymousLogger();

    public List<Event> getAllEvents() {
        return eventRepository.getAll();
    }

    public void deleteEvent(Event event) {
        eventRepository.delete(event.getId());
    }
}
