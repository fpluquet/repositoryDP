package repositories.memory;


import repositories.common.RepositoryFactory;

public class MemoryRepositoryFactory implements RepositoryFactory {

    private final ArticleRepository articleRepository;
    private final ProfileRepository profileRepository;

    public MemoryRepositoryFactory() {
        articleRepository = new ArticleRepository();
        profileRepository = new ProfileRepository();
    }


    @Override
    public ArticleRepository getArticleRepository() {
        return articleRepository;
    }

    @Override
    public ProfileRepository getProfileRepository() {
        return profileRepository;
    }
}
