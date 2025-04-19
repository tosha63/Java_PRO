package t1.dao;

import t1.entity.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO {

    void createUser(String username) throws SQLException;
    void deleteUser(Long id) throws SQLException;
    User getUser(Long id) throws SQLException;
    void updateUser(User user) throws SQLException;
    List<User> getUsers() throws SQLException;
}
