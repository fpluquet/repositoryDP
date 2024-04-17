package repositories.common;

public interface RepositoryFactory {

    ArticleRepository getArticleRepository() throws Exception;

    ProfileRepository getProfileRepository();
}
