package repositories;

import dtos.GroupDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GroupRepository implements IGroupRepository {

    private Connection connection;

    @Override
    public List<GroupDTO> findByName(String name) {
        List<GroupDTO> groupsByName = new ArrayList<>();
        String query = "select * from `GROUPS` where GROUP_NAME like ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + name + "%");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                int id = result.getInt("GROUP_ID");
                String groupName = result.getString("GROUP_NAME");
                String description = result.getString("GROUP_DESCRIPTION");
                groupsByName.add(new GroupDTO(id, groupName, description));
            }
            return groupsByName;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void add(GroupDTO dto) {
        String query = "insert into `GROUPS`(GROUP_ID, GROUP_NAME, GROUP_DESCRIPTION) values(?,?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, dto.getId());
            statement.setString(2, dto.getName());
            statement.setString(3, dto.getDescription());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(GroupDTO dto) {
        String query = "update `GROUPS` set GROUP_NAME= ?, GROUP_DESCRIPTION= ? where GROUP_ID=?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, dto.getName());
            statement.setString(2, dto.getDescription());
            statement.setInt(3, dto.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addOrUpdate(GroupDTO dto) {
        String query = "insert into `GROUPS` values(?, ?, ?) on duplicate key update GROUP_NAME= ?, GROUP_DESCRIPTION= ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, dto.getId());
            statement.setString(2, dto.getName());
            statement.setString(3, dto.getDescription());
            statement.setString(4, dto.getName());
            statement.setString(5, dto.getDescription());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(GroupDTO dto) {
        String query = "delete from `GROUPS` where GROUP_ID= ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, dto.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<GroupDTO> findById(int id) {
        String query = "select * from `GROUPS` where GROUP_ID= ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                String name = result.getString("GROUP_NAME");
                String description = result.getString("GROUP_DESCRIPTION");
                return Optional.of(new GroupDTO(id, name, description));
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
    public void rollbackTransaction() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getCount() {
        String query = "select count(GROUP_ID) from `GROUPS`";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getInt("count(GROUP_ID)");
            }
            throw new IllegalStateException("No result for count query");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean exists(GroupDTO dto) {
        String query = "select count(GROUP_ID) from `GROUPS` where GROUP_ID= ? and GROUP_NAME= ? and GROUP_DESCRIPTION= ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, dto.getId());
            statement.setString(2, dto.getName());
            statement.setString(3, dto.getDescription());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getInt("count(GROUP_ID)") > 0;
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
