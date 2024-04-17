package controllers;

import models.Article;
import models.Profile;
import repositories.RepositoryFactory;
import repositories.db.DBRepositoryFactory;
import repositories.files.FilesRepositoryFactory;
import repositories.memory.MemoryRepositoryFactory;
import services.ArticleService;
import services.ProfileService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static RepositoryFactory getDBRepositoryFactory() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:data\\sample.db");
        return new DBRepositoryFactory(connection);
    }

    public static RepositoryFactory getFilesRepositoryFactory() throws IOException {
        return new FilesRepositoryFactory("data");
    }

    public static RepositoryFactory getMemoryRepositoryFactory() {
        return new MemoryRepositoryFactory();
    }

    public static void main(String[] args) {
        Logger logger = Logger.getGlobal();
        try {
            RepositoryFactory repositoryFactory = getDBRepositoryFactory();
//            RepositoryFactory repositoryFactory = getFilesRepositoryFactory();
//            RepositoryFactory repositoryFactory = getMemoryRepositoryFactory();
            ArticleService articleService = new ArticleService(repositoryFactory.getArticleRepository());
            ProfileService profileService = new ProfileService(repositoryFactory.getProfileRepository());

            System.out.println("Articles:");
            articleService.findAll().forEach(System.out::println);

            System.out.println("Profiles:");
            profileService.findAll().forEach(System.out::println);

            Profile profile;
            if (profileService.exists(p -> p.getLogin().equals("fredo"))) {
                profile = profileService.get(p -> p.getLogin().equals("fredo"));
            } else {
                profile = profileService.createProfile("fredo", "Fred Flintstone");
                System.out.println("Created profile: " + profile);
                System.out.println("Profiles:");
            }

            profile.setFullname("Fred Flintstone Sr.");
            profileService.update(profile);

            System.out.println("Profiles:");
            profileService.findAll().forEach(System.out::println);


            Article article = articleService.createArticle("Title", "Contents", profile);
            System.out.println("Articles:");
            articleService.findAll().forEach(System.out::println);

            article.setTitle("New Title");

            articleService.update(article);

            System.out.println("Articles:");
            articleService.findAll().forEach(System.out::println);

        } catch (IOException e) {
            logger.severe("Error while creating repository factory.");
            logger.log(Level.SEVERE, e.getMessage(), e);
        } catch (Exception e) {
            Logger.getGlobal().severe("General error.");
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

    }
}