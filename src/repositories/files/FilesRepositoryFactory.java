package repositories.files;

import models.Profile;
import repositories.RepositoryFactory;

import java.io.IOException;
import java.util.List;

public class FilesRepositoryFactory implements RepositoryFactory {

    private final ArticleRepository articleRepository;
    private final ProfileRepository profileRepository;

    public FilesRepositoryFactory(String folder) throws IOException {
        articleRepository = new repositories.files.ArticleRepository(this, folder);
        profileRepository = new repositories.files.ProfileRepository(this, folder);
    }

    @Override
    public repositories.ArticleRepository getArticleRepository() {
        return articleRepository;
    }

    @Override
    public repositories.ProfileRepository getProfileRepository() {
        return profileRepository;
    }


    List<String> toLines(Profile profile) {
        return this.profileRepository.profileToLines(profile);
    }

    models.Profile profileFromStream(FileLinesStream stream) {
        return this.profileRepository.profileFromStream(stream);
    }

    List<String> toLines(models.Article article) {
        return this.articleRepository.articleToLines(article);
    }

    models.Article articleFromStream(FileLinesStream stream) throws Exception {
        return this.articleRepository.articleFromStream(stream);
    }


}
