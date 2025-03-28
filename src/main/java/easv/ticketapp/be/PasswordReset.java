package easv.ticketapp.be;

import java.time.LocalDateTime;

public class PasswordReset {
    int user_id;
    String token;
    LocalDateTime created_at;

    public PasswordReset(int user_id, String token, LocalDateTime created_at) {
        this.user_id = user_id;
        this.token = token;
        this.created_at = created_at;
    }

    public int getUserId() {
        return user_id;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getCreatedAt() {
        return created_at;
    }
}
