package easv.ticketapp.dal.db.interaces;

import easv.ticketapp.be.Event;
import easv.ticketapp.be.User;

import java.util.List;

public interface UserRepository extends BaseRepository<User> {
        User findByEmail(String email);
        List<User> getAllCoordinators();
        boolean updatePassword(User user, String password);
        boolean addCoordinator(Event event, User user);
        boolean updatePicture(int userId, String picture);
}
