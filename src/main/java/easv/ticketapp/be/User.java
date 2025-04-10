package easv.ticketapp.be;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {
    public static final int ADMIN_TYPE = 1;
    public static final int COORDINATOR_TYPE = 2;
    private int id;
    private String name;
    private String email;
    private String password;
    private int userType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * @param id        int
     * @param name      String
     * @param email     String
     * @param password  String
     * @param userType  int
     * @param createdAt LocalDateTime
     * @param updatedAt LocalDateTime
     */
    public User(int id, String name, String email, String password, int userType, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public User(int id) {
        this.id = id;
    }

    public User(String name, String email, String password, Integer userType) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    public User(String name, String email, Integer userType) {
        this.name = name;
        this.email = email;
        this.userType = userType;
    }

    public User(String name, String email, String password, int userType, LocalDateTime localDateTime, LocalDateTime localDateTime1) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.createdAt = localDateTime;
        this.updatedAt = localDateTime1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isAdmin() {
        return userType == ADMIN_TYPE;
    }

    public boolean isCoordinator() {
        return userType == COORDINATOR_TYPE;
    }

    @Override
    public String toString() {
        return name;
    }

}
