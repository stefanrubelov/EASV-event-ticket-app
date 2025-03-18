package easv.ticketapp.be;

import java.util.Date;

public class Event {
    private final Integer id;
    private String name;
    private String date;
    private String location;
    private String description;

    public Event(Integer id, String name, String date, String location, String description) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.location = location;
        this.description = description;
    }

    public Integer getId() {
        return id;
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

    public void setDate(Date date) {
        this.date = String.valueOf(date);
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
