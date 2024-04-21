package services;

import repositories.db.DBRepositoryFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBProfileServiceTest extends ProfileServiceTest {
    @Override
    protected ProfileService getProfileService() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        DBRepositoryFactory factory = new DBRepositoryFactory(connection);
        return new ProfileService(factory.getProfileRepository());
    }
}
