package controllers;

import models.Article;
import models.Profile;
import org.slf4j.LoggerFactory;
import repositories.common.RepositoryFactory;
import repositories.common.filters.FilterContains;
import repositories.common.filters.FilterEquals;
import repositories.db.DBRepositoryFactory;
import repositories.files.FilesRepositoryFactory;
import repositories.memory.MemoryRepositoryFactory;
import services.AbstractService;
import services.ArticleService;
import services.ProfileService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainController {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(MainController.class);
    private RepositoryFactory repositoryFactory;
    private ArticleService articleService;
    private ProfileService profileService;


    /**
     * Set the application repository factory. Change the return value to switch between different repository factories.
     * @throws Exception If an error occurs while creating the repository factory.
     */
    public void setApplicationRepositoryFactory() throws Exception {
        this.repositoryFactory = getDBRepositoryFactory();
//        this.repositoryFactory = getFilesRepositoryFactory();
//        this.repositoryFactory = getMemoryRepositoryFactory();
    }

    public void start() {
        try {
            setApplicationRepositoryFactory();
            setServices();

            log.info("Before changes...");
            showAll("Articles", articleService);
            showAll("Profiles", profileService);

            log.info("Insert data if not exists...");
            insertNewDataIfNotExists();

            log.info("Get specific profile...");
            Profile profile = getSpecificProfile();

            log.info("Change fullname...");
            profile.setFullname("That's a new name!");
            profileService.update(profile);

            log.info("After changes...");
            showAll("Profiles", profileService);

            log.info("Restore fullname...");
            profile.setFullname("Fred Flintstone");
            profileService.update(profile);


            log.info("Create article...");
            Article article = articleService.createArticle("Title", "Contents", profile);
            showAll("Articles", articleService);

            log.info("Update article...");
            article.setTitle("New Title");
            articleService.update(article);

            log.info("After changes...");
            showAll("Articles", articleService);

        } catch (IOException e) {
            log.error("Error while creating repository factory.");
            log.error(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    private Profile getSpecificProfile() throws Exception {
        return profileService.get(new FilterContains<Profile>("fullName", "Fred").and(new FilterEquals<Profile>("login", "freddy")).or(new FilterEquals<Profile>("login", "barney")));
    }

    private void insertNewDataIfNotExists() throws Exception {
        if (!profileService.exists(new FilterEquals<>("login", "freddy"))) {
            Profile profile = profileService.createProfile("freddy", "Fred Flintstone");
            System.out.println("Created profile: " + profile);
        }
        if (!profileService.exists(new FilterEquals<>("login", "barney"))) {
            Profile profile = profileService.createProfile("barney", "Barney Rubble");
            System.out.println("Created profile: " + profile);
        }
    }

    private void showAll(String title, AbstractService<?> service) throws Exception {
        log.info("{} :", title);
        service.getAll().forEach(e -> log.info(e.toString()));
    }

    private void setServices() throws Exception {
        this.articleService = new ArticleService(repositoryFactory.getArticleRepository());
        this.profileService = new ProfileService(repositoryFactory.getProfileRepository());
    }

    public RepositoryFactory getDBRepositoryFactory() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:data\\sample.db");
        return new DBRepositoryFactory(connection);
    }

    public RepositoryFactory getFilesRepositoryFactory() throws IOException {
        return new FilesRepositoryFactory("data");
    }

    public RepositoryFactory getMemoryRepositoryFactory() {
        return new MemoryRepositoryFactory();
    }

}
