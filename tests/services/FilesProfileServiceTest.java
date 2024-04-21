package services;

import repositories.files.FilesRepositoryFactory;

import java.io.IOException;
import java.sql.SQLException;

public class FilesProfileServiceTest extends ProfileServiceTest {
    @Override
    protected ProfileService getProfileService() throws SQLException, IOException {
        return new ProfileService(new FilesRepositoryFactory("testsData").getProfileRepository());
    }
}
