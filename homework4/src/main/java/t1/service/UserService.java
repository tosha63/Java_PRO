package t1.service;

import org.springframework.stereotype.Service;
import t1.dao.UserDAO;
import t1.entity.User;

import java.sql.SQLException;
import java.util.List;

@Service
public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void createUser(String username) throws SQLException {
        userDAO.createUser(username);
    }

    public void deleteUser(Long id) throws SQLException {
        userDAO.deleteUser(id);
    }

    public User getUser(Long id) throws SQLException {
        return userDAO.getUser(id);
    }

    public void updateUser(User user) throws SQLException {
        userDAO.updateUser(user);
    }

    public List<User> getAllUsers() throws SQLException {
        return userDAO.getUsers();
    }
}
