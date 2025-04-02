package easv.ticketapp.bll;

import easv.ticketapp.be.User;
import easv.ticketapp.dal.db.UserRepository;
import easv.ticketapp.security.Auth;
import easv.ticketapp.utils.UuidGenerator;

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

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean updatePassword(User user, String password) {
        System.out.println(user.getEmail());
        System.out.println(hashPassword(password));

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
}
