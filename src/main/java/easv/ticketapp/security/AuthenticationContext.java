package easv.ticketapp.security;

import easv.ticketapp.be.User;

public class AuthenticationContext {

    private static AuthenticationContext instance;
    private User currentUser;
    private boolean authenticated = false;

    private AuthenticationContext() {
        // Private constructor to prevent instantiation
    }

    public static synchronized AuthenticationContext getInstance() {
        if (instance == null) {
            instance = new AuthenticationContext();
        }
        return instance;
    }

    /**
     * Set the current authenticated user
     * @param user The user to set as authenticated
     */
    public void setUser(User user) {
        this.currentUser = user;
        this.authenticated = (user != null);
    }

    /**
     * Check if a user is authenticated
     * @return true if user is authenticated, false otherwise
     */
    public boolean check() {
        return authenticated;
    }

    /**
     * Get the current authenticated user
     * @return The current user or null if not authenticated
     */
    public User user() {
        return currentUser;
    }

    /**
     * Log out the current user
     */
    public void logout() {
        this.currentUser = null;
        this.authenticated = false;
    }
}