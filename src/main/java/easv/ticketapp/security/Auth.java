package easv.ticketapp.security;

import easv.ticketapp.be.User;

public class Auth {
        /**
         * Get the AuthenticationContext instance
         * @return AuthenticationContext instance
         */
        public static AuthenticationContext user() {
            return AuthenticationContext.getInstance();
        }

        /**
         * Check if a user is authenticated
         * @return true if a user is authenticated, false otherwise
         */
        public static boolean check() {
            return user().check();
        }

        /**
         * Get the current authenticated user
         * @return The current user or null if not authenticated
         */
        public static User getUser() {
            return user().user();
        }

        /**
         * Log in a user
         * @param user The user to log in
         */
        public static void login(User user) {
            user().setUser(user);
        }

        /**
         * Log out the current user
         */
        public static void logout() {
            user().logout();
        }
    }