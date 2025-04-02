package easv.ticketapp.be.ticket;

public class TicketType {
    private int id;
    private String type;

    public TicketType(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public TicketType(String type) {
        this.type = type;
    }

    public TicketType(int ticketTypeId) {
        this.id = ticketTypeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
