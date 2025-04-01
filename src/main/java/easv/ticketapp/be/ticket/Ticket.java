package easv.ticketapp.be.ticket;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.UUID;

public class Ticket {
    private Integer id;
    private String eventName;
    private double price;
    private String perks;
    private String barcode;
    private String description;
    private String location;
    private LocalDateTime date;
    private TicketType ticketType;
    private Integer availableTickets;

    public Ticket(Integer id, String eventName, double price, String perks, String description, String location, LocalDateTime date, TicketType ticketType, Integer availableTickets) {
        this.id = id;
        this.eventName = eventName;
        this.price = price;
        this.perks = perks;
        this.barcode = generateBarCode();
        this.description = description;
        this.location = location;
        this.date = date;
        this.ticketType = ticketType;
        this.availableTickets = availableTickets;
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
    public String getEventName() {
        return eventName;
    }
    public String getDescription() {
        return description;
    }
    public Double getPrice() {
        return price;
    }
    public String getPerks() {
        return perks;
    }
    public String getLocation() {
        return location;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public String getBarCode() {
        return barcode;
    }
    public TicketType getTicketType() {
        return ticketType;
    }
    public Integer getAvailableTickets() {
        return availableTickets;
    }
}
