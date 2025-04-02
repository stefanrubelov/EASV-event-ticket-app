package easv.ticketapp.be.ticket;

import easv.ticketapp.be.Event;

import java.util.UUID;

public class Ticket {
    private Integer id;
    private Integer eventId;
    private double price;
    private String description;
    private Integer ticketTypeId;
    private TicketType ticketType;
    private Event event;

    /**
     * Constructor to create a Ticket object with the specified details.
     *
     * @param eventId     The unique identifier for the event associated with the ticket.
     * @param price       The price of the ticket.
     * @param description A brief description of the ticket or event.
     * @param ticketType  The type of the ticket (e.g., VIP, General Admission, etc.).
     */
    public Ticket(Integer eventId, double price, String description, TicketType ticketType) {
        this.eventId = eventId;
        this.price = price;
        this.description = description;
        this.ticketType = ticketType;
    }

    public Ticket(Integer eventId, double price, String description, Integer ticketTypeId) {
        this.eventId = eventId;
        this.price = price;
        this.description = description;
        this.ticketTypeId = ticketTypeId;
    }

    public Ticket(Integer eventId, double price, String description, TicketType ticketType, Event event) {
        this.eventId = eventId;
        this.price = price;
        this.description = description;
        this.ticketType = ticketType;
        this.event = event;
    }

    public Ticket(Integer id, Event event, double price, String description, TicketType ticketType) {
        this.id = id;
        this.event = event;
        this.price = price;
        this.description = description;
        this.ticketType = ticketType;
    }

    public Ticket(Integer id) {
        this.id = id;
    }

    private String generateBarCode() {
        return "REG-" + UUID.randomUUID().toString();
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public Event getEvent() {
        return event;
    }

    public Integer getTicketTypeId() {
        return ticketTypeId;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
