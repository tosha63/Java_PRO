package t1.dao;

import org.springframework.stereotype.Component;
import t1.entity.User;
import t1.service.ConnectionFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDAOImpl implements UserDAO {

    private final ConnectionFactory connectionFactory;

    public UserDAOImpl(DataSource dataSource) {
        this.connectionFactory = new ConnectionFactory(dataSource);
    }


    @Override
    public void createUser(String username) throws SQLException {
        try (Connection connection = connectionFactory.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO users (username) VALUES (?)");
            ps.setString(1, username);
            ps.executeUpdate();
        }
    }

    @Override
    public void deleteUser(Long id) throws SQLException {
        try (Connection conn = connectionFactory.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE id=?");
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public User getUser(Long id) throws SQLException {
        try (Connection connection = connectionFactory.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE id=?");
            ps.setLong(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return mapRowToUser(resultSet);
            }
            return null;
        }
    }

    @Override
    public void updateUser(User user) throws SQLException {
        try (Connection conn = connectionFactory.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE users SET username=? WHERE id=?");
            ps.setString(1, user.username());
            ps.setLong(2, user.id());
            ps.executeUpdate();
        }
    }

    @Override
    public List<User> getUsers() throws SQLException {
        try (Connection conn = connectionFactory.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users");
            ResultSet resultSet = ps.executeQuery();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(mapRowToUser(resultSet));
            }
            return users;
        }
    }


    private User mapRowToUser(ResultSet resultSet) throws SQLException {
        return new User(resultSet.getLong("id"),
                        resultSet.getString("username"));
    }
}