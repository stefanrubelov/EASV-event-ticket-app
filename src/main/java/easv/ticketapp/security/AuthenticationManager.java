package easv.ticketapp.security;

import java.util.concurrent.ConcurrentHashMap;

public class AuthenticationManager {
    private static final ConcurrentHashMap<String, AuthenticationContext> userSessions = new ConcurrentHashMap<>();

    public static AuthenticationContext getSession(String sessionId) {
        return userSessions.computeIfAbsent(sessionId, key -> new AuthenticationContext());
    }

    public static void removeSession(String sessionId) {
        userSessions.remove(sessionId);
    }
}
