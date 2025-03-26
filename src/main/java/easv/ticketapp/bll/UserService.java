package easv.ticketapp.bll;

import easv.ticketapp.be.User;
import easv.ticketapp.dal.db.UserRepository;
import easv.ticketapp.security.Auth;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

public class UserService {

    private final UserRepository userRepository = new UserRepository();

    public User authenticate(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user != null && user.getPassword().equals(hashPassword(password))) {
            Auth.login(user);
            return user;
        }

        return null;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteUser(User user) {
        userRepository.delete(user.getId());
    }

    public List<User> getCoordinators() {
        return userRepository.getAllCoordinators();
    }

    public void addUser(User user) {
        User newCoordinator = userRepository.create(user);
    }
}
