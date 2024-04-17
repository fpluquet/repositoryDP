package repositories.db;

import repositories.RepositoryFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class DBRepositoryFactory implements RepositoryFactory {

    Connection connection;


    public DBRepositoryFactory(Connection connection) {
        this.connection = connection;
    }

    @Override
    public ArticleRepository getArticleRepository() throws Exception {
        try {
            return new ArticleRepository(this, connection);
        } catch (SQLException e) {
            throw new Exception(e);
        }
    }

    @Override
    public ProfileRepository getProfileRepository() {
        return new ProfileRepository(connection);
    }
}
