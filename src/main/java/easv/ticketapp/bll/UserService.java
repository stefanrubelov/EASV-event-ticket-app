package easv.ticketapp.bll;

import easv.ticketapp.be.User;
import easv.ticketapp.dal.db.UserRepository;
import easv.ticketapp.security.Auth;
import easv.ticketapp.utils.UuidGenerator;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

public class UserService {

    private final UserRepository userRepository = new UserRepository();

    public User authenticate(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && verifyPassword(password, user.getPassword())) {
            Auth.login(user);
            return user;
        }

        return null;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean updatePassword(User user, String password) {
        return userRepository.updatePassword(user, hashPassword(password));
    }

    public void deleteUser(User user) {
        userRepository.delete(user.getId());
    }

    public List<User> getCoordinators() {
        return userRepository.getAllCoordinators();
    }

    public void addUser(User user) {
        String password = UuidGenerator.generate();
        user.setPassword(hashPassword(password));
        userRepository.create(user);
    }

    public void updateUser(User user) {
        userRepository.update(user);
    }

    private String hashPassword(String password) {
        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.update(salt);
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            ByteBuffer buffer = ByteBuffer.allocate(salt.length + hash.length);
            buffer.put(salt);
            buffer.put(hash);

            return Base64.getEncoder().encodeToString(buffer.array());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean verifyPassword(String password, String storedHash) {
        try {
            byte[] decodedHash = Base64.getDecoder().decode(storedHash);

            ByteBuffer buffer = ByteBuffer.wrap(decodedHash);
            byte[] salt = new byte[16];
            buffer.get(salt);

            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.update(salt);
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            ByteBuffer hashBuffer = ByteBuffer.allocate(salt.length + hash.length);
            hashBuffer.put(salt);
            hashBuffer.put(hash);

            return MessageDigest.isEqual(decodedHash, hashBuffer.array());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }
}
