package repositories;

import dtos.UserDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements IUserRepository {

    private Connection connection;

    @Override
    public List<UserDTO> findByName(String username) {
        String query = "select * from USERS where USER_LOGIN like ?";
        List<UserDTO> usersByName = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + username + "%");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                int id = result.getInt("USER_ID");
                String login = result.getString("USER_LOGIN");
                String password = result.getString("USER_PASSWORD");
                usersByName.add(new UserDTO(id, login, password));
            }
            return usersByName;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void add(UserDTO dto) {
        String query = "insert into USERS(USER_ID, USER_LOGIN, USER_PASSWORD) values(?,?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, dto.getId());
            statement.setString(2, dto.getLogin());
            statement.setString(3, dto.getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(UserDTO dto) {
        String query = "update USERS set USER_LOGIN= ?, USER_PASSWORD= ? where USER_ID=?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, dto.getLogin());
            statement.setString(2, dto.getPassword());
            statement.setInt(3, dto.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addOrUpdate(UserDTO dto) {
        String query = "insert into USERS values(?, ?, ?) on duplicate key update USER_LOGIN= ?, USER_PASSWORD= ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, dto.getId());
            statement.setString(2, dto.getLogin());
            statement.setString(3, dto.getPassword());
            statement.setString(4, dto.getLogin());
            statement.setString(5, dto.getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(UserDTO dto) {
        String query = "delete from USERS where USER_ID= ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, dto.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserDTO> findById(int id) {
        String query = "select * from USERS where USER_ID= ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                String login = result.getString("USER_LOGIN");
                String password = result.getString("USER_PASSWORD");
                return Optional.of(new UserDTO(id, login, password));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void beginTransaction() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/UTP10", "monika", "m0niqa");
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void commitTransaction() {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rollbackTransaction() throws SQLException {

        connection.rollback();

    }

    @Override
    public int getCount() {
        String query = "select count(USER_ID) from USERS";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getInt("count(USER_ID)");
            }
            throw new IllegalStateException("No result for count query");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean exists(UserDTO dto) {
        String query = "select count(USER_ID) from USERS where USER_ID= ? and USER_LOGIN= ? and USER_PASSWORD= ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, dto.getId());
            statement.setString(2, dto.getLogin());
            statement.setString(3, dto.getPassword());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getInt("count(USER_ID)") > 0;
            }
            throw new IllegalStateException("No result for count query");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}