package easv.ticketapp.utils;

import java.util.UUID;

public class UuidGenerator {

    /**
     * Generates a standard UUID in the format "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
     * using only lowercase hexadecimal characters.
     *
     * @return String formatted UUID in lowercase
     */
    public static String generate() {
        UUID uuid = UUID.randomUUID();

        return uuid.toString().toLowerCase();
    }
}