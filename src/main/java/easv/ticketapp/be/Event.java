package easv.ticketapp.be;

import java.time.LocalDateTime;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Event {
    private Integer id; // Remove 'final' to allow setting later
    private String name;
    private LocalDateTime date;
    private String location;
    private String description;

    public Event(Integer id, String name, LocalDateTime date, String location, String description) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.location = location;
        this.description = description;
    }

    // Constructor without ID (for new events, letting the DB auto-generate it)
    public Event(String name, String location, LocalDateTime date, String description) {
        this.id = null; // Default to null since it's auto-generated
        this.name = name;
        this.date = date;
        this.location = location;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) { // Allow setting ID after DB insert
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getStart_date() {
        return date;
    }

    public void setStart_date(LocalDateTime date) {
        this.date = date;
    }
    // Fix: Properly format Date when setting
    public void setDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        this.date = sdf.format(date);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getDate() {
        return date;
    }
}
