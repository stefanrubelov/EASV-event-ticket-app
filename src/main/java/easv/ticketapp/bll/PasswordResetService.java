package easv.ticketapp.bll;

import easv.ticketapp.be.PasswordReset;
import easv.ticketapp.dal.db.PasswordResetRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.logging.Logger;

public class PasswordResetService {
    final private PasswordResetRepository passwordResetRepository = new PasswordResetRepository();
    final private Logger logger = Logger.getAnonymousLogger();

    public boolean getToken(String token) {
        PasswordReset result = passwordResetRepository.findToken(token);
        if (result != null) {
            LocalDateTime targetDateTime = result.getCreatedAt();
            LocalDateTime now = LocalDateTime.now();
            Duration duration = Duration.between(now, targetDateTime);

            return Math.abs(duration.toMinutes()) <= 10;
        }
        return false;
    }

    public String createToken(int user_id) {
        String token;
        boolean success = false;
        PasswordReset passwordReset;

        do {
            token = generateRandomString();

            passwordReset = new PasswordReset(user_id, token, LocalDateTime.now());

            success = passwordResetRepository.createToken(passwordReset);
        } while (!success);

        return token;
    }

    public boolean removeToken(int user_id) {
        return passwordResetRepository.deleteTokens(user_id);
    }

    private static String generateRandomString() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 3; i++) {
            sb.append((char) ('A' + random.nextInt(26)));
        }

        sb.append('-');

        for (int i = 0; i < 3; i++) {
            sb.append((char) ('A' + random.nextInt(26)));
        }

        return sb.toString();
    }
}
