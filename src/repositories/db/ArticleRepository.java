package repositories.db;

import models.Article;
import models.Profile;
import org.slf4j.LoggerFactory;
import repositories.common.filters.AbstractFilter;
import repositories.common.filters.visitors.SQLGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArticleRepository extends repositories.common.ArticleRepository {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ArticleRepository.class);
    private final Connection connection;
    private final DBRepositoryFactory factory;

    public ArticleRepository(DBRepositoryFactory factory, Connection connection) throws SQLException {
        super();
        this.factory = factory;
        this.connection = connection;
        createTableIfNotExists();
    }

    private void createTableIfNotExists() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS articles (id INTEGER PRIMARY KEY, title VARCHAR(255), content TEXT, author_id BIGINT, FOREIGN KEY (author_id) REFERENCES profiles(id))");
        statement.execute();
    }

    @Override
    public List<Article> findAll() throws Exception {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM articles");
        ResultSet resultSet = statement.executeQuery();
        List<Article> articles = new java.util.ArrayList<>();

        while (resultSet.next()) {
            articles.add(articleFromResultSet(resultSet));
        }

        return articles;
    }

    private Article articleFromResultSet(ResultSet resultSet) throws Exception {
        Profile profile = this.factory.getProfileRepository().getById(resultSet.getLong("author_id"));
        return new Article(resultSet.getLong("id"), resultSet.getString("title"), resultSet.getString("content"), profile);
    }

    @Override
    public Article getById(Long aLong) throws Exception {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM articles WHERE id = ?");
        statement.setLong(1, aLong);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return articleFromResultSet(resultSet);
        }
        throw new Exception("Article not found");
    }

    @Override
    public void save(Article article) throws Exception {
        if (!article.isSaved())
            insert(article);
        else
            update(article);
    }

    public void insert(Article article) throws Exception {
        this.factory.getProfileRepository().save(article.getAuthor());
        PreparedStatement statement = connection.prepareStatement("INSERT INTO articles (title, content, author_id) VALUES (?, ?, ?)");
        statement.setString(1, article.getTitle());
        statement.setString(2, article.getContent());
        statement.setLong(3, article.getAuthor().getId());
        statement.execute();
        ResultSet resultSet = statement.getGeneratedKeys();
        if (resultSet.next()) {
            article.setId(resultSet.getLong(1));
        }
    }

    @Override
    public void update(Article article) throws Exception {
        PreparedStatement statement = connection.prepareStatement("UPDATE articles SET title = ?, content = ?, author_id = ? WHERE id = ?");
        statement.setString(1, article.getTitle());
        statement.setString(2, article.getContent());
        statement.setLong(3, article.getAuthor().getId());
        statement.setLong(4, article.getId());
        statement.execute();
    }

    @Override
    public void delete(Article article) throws Exception {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM articles WHERE id = ?");
        statement.setLong(1, article.getId());
        statement.execute();
    }

    @Override
    public Article get(AbstractFilter<Article> filter) throws Exception {
        SQLGenerator<Article> sqlGenerator = new SQLGenerator<Article>();
        String sql = sqlGenerator.generateSQL(filter);
        log.info("SQL à exécuter : " + "SELECT * FROM articles WHERE " + sql + " LIMIT 1");
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM articles WHERE " + sql + " LIMIT 1");
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return articleFromResultSet(resultSet);
        }
        throw new Exception("Article not found");
    }

    @Override
    public List<Article> getAll(AbstractFilter<Article> filter) throws Exception {
        SQLGenerator<Article> sqlGenerator = new SQLGenerator<Article>();
        String sql = sqlGenerator.generateSQL(filter);
        log.info("SQL à exécuter : " + "SELECT * FROM articles WHERE " + sql);
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM articles WHERE " + sql);
        ResultSet resultSet = statement.executeQuery();
        List<Article> articles = new java.util.ArrayList<>();

        while (resultSet.next()) {
            articles.add(articleFromResultSet(resultSet));
        }

        return articles;
    }


}
