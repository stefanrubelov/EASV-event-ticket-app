package easv.ticketapp.security;

import easv.ticketapp.be.User;

public class Auth {
    private static String sessionId;

    public static void setSessionId(String id) {
        sessionId = id;
    }

    private static AuthenticationContext user() {
        if (sessionId == null) {
            throw new IllegalStateException("Session ID not set!");
        }
        return AuthenticationManager.getSession(sessionId);
    }

    public static boolean check() {
        return user().check();
    }

    public static User getUser() {
        return user().user();
    }

    public static void login(User user) {
        String sessionId = String.valueOf(user.getId());
        setSessionId(sessionId);
        user().setUser(user);
    }

    public static void logout() {
        user().logout();
        AuthenticationManager.removeSession(sessionId);
        sessionId = null;
    }
}
