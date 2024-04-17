package controllers;

import models.Article;
import models.Profile;
import repositories.common.RepositoryFactory;
import repositories.db.DBRepositoryFactory;
import repositories.files.FilesRepositoryFactory;
import repositories.common.filters.AbstractFilter;
import repositories.common.filters.FilterContains;
import repositories.common.filters.FilterEquals;
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
 //           RepositoryFactory repositoryFactory = getMemoryRepositoryFactory();
            ArticleService articleService = new ArticleService(repositoryFactory.getArticleRepository());
            ProfileService profileService = new ProfileService(repositoryFactory.getProfileRepository());

            System.out.println("Articles:");
            articleService.findAll().forEach(System.out::println);

            System.out.println("Profiles:");
            profileService.findAll().forEach(System.out::println);

            AbstractFilter<Profile> filter = new FilterEquals<Profile>("login", "freddy").and(new FilterEquals<>("fullname", "Fred Flintstonee")).or(new FilterEquals<>("login", "barney"));
            AbstractFilter<Profile> filter2 = new FilterEquals<Profile>("login", "freddy").and(new FilterEquals<Profile>("fullname", "Fred Flintstonee").or(new FilterEquals<>("login", "barney")));
            AbstractFilter<Profile> filter3 = new FilterContains<Profile>("fullname", "Fred").or(new FilterContains<>("fullname", "barney"));

            Profile profile;
            if (profileService.exists(new FilterEquals<>("login", "freddy"))) {
                profile = profileService.get(filter3);
            } else {
                profile = profileService.createProfile("freddy", "Fred Flintstone");
                System.out.println("Created profile: " + profile);
            }

            if (profileService.exists(filter)) {
                System.out.println("Profile found: " + profileService.get(filter));
            }

            for(Profile p : profileService.getAll(filter3)){
                System.out.println("Profile found with filter3: " + p);
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