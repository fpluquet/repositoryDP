package repositories.db;

import models.Profile;
import repositories.filters.AbstractFilter;
import repositories.filters.visitors.SQLGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProfileRepository extends repositories.ProfileRepository {
    private final Connection connection;

    public ProfileRepository(Connection connection) {
        this.connection = connection;
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        try {
            PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS profiles (id INTEGER PRIMARY KEY, fullname VARCHAR(255), login VARCHAR(255))");
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Profile> findAll() throws Exception {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM profiles");
        statement.execute();
        ResultSet resultSet = statement.getResultSet();
        List<Profile> profiles = new ArrayList<>();
        while (resultSet.next()) {
            profiles.add(profileFromResultSet(resultSet));
        }
        return profiles;
    }

    private Profile profileFromResultSet(ResultSet resultSet) throws Exception {
        return new Profile(resultSet.getLong("id"), resultSet.getString("login"), resultSet.getString("fullname"));
    }

    @Override
    public Profile getById(Long aLong) throws Exception {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM profiles WHERE id = ?");
        statement.setLong(1, aLong);
        statement.execute();
        ResultSet resultSet = statement.getResultSet();
        if (resultSet.next()) {
            return profileFromResultSet(resultSet);
        }
        throw new Exception("Profile not found");
    }

    @Override
    public void save(Profile profile) throws Exception {
        if (!profile.isSaved()) {
            insert(profile);
        } else {
            update(profile);
        }
    }

    public void insert(Profile profile) throws Exception {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO profiles (fullname, login) VALUES (?, ?)");
        statement.setString(1, profile.getFullname());
        statement.setString(2, profile.getLogin());
        statement.execute();
        ResultSet resultSet = statement.getGeneratedKeys();
        if (resultSet.next()) {
            profile.setId(resultSet.getLong(1));
        }
    }

    @Override
    public void update(Profile profile) throws Exception {
        PreparedStatement statement = connection.prepareStatement("UPDATE profiles SET fullname = ?, login = ? WHERE id = ?");
        statement.setString(1, profile.getFullname());
        statement.setString(2, profile.getLogin());
        statement.setLong(3, profile.getId());
        statement.execute();
    }

    @Override
    public void delete(Profile profile) throws Exception {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM profiles WHERE id = ?");
        statement.setLong(1, profile.getId());
        statement.execute();
    }

    @Override
    public Profile get(AbstractFilter<Profile> filter) throws Exception {
        SQLGenerator<Profile> sqlGenerator = new SQLGenerator<Profile>();
        String sql = sqlGenerator.generateSQL(filter);
        Logger.getLogger("SQL").log(Level.INFO, "SQL à exécuter : " + "SELECT * FROM product WHERE " + sql + " LIMIT 1");
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM profiles WHERE " + sql + " LIMIT 1");
        statement.execute();
        ResultSet resultSet = statement.getResultSet();
        if (resultSet.next()) {
            return profileFromResultSet(resultSet);
        }
        throw new Exception("Profile not found");
    }

    @Override
    public List<Profile> getAll(AbstractFilter<Profile> filter) throws Exception {
        SQLGenerator<Profile> sqlGenerator = new SQLGenerator<Profile>();
        String sql = sqlGenerator.generateSQL(filter);
        Logger.getLogger("SQL").log(Level.INFO, "SQL à exécuter : " + "SELECT * FROM product WHERE " + sql);
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM profiles WHERE " + sql);
        statement.execute();
        ResultSet resultSet = statement.getResultSet();
        List<Profile> profiles = new ArrayList<>();
        while (resultSet.next()) {
            profiles.add(profileFromResultSet(resultSet));
        }
        return profiles;
    }
}
