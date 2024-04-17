package repositories;

public interface RepositoryFactory {

    ArticleRepository getArticleRepository() throws Exception;

    ProfileRepository getProfileRepository();
}
