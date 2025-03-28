package easv.ticketapp.be;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Event {
    private Integer id; // Remove 'final' to allow setting later
    private String name;
    private String date;  // Still using String, but formatted correctly
    private String location;
    private String description;

    // Constructor with ID (for existing events)
    public Event(Integer id, String name, String date, String location, String description) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.location = location;
        this.description = description;
    }

    // Constructor without ID (for new events, letting the DB auto-generate it)
    public Event(String name, String location, String date, String description) {
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

    public String getDate() {
        return date;
    }

    // Fix: Properly format Date when setting
    public void setDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.date = sdf.format(date);
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
}
