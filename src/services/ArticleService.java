package services;

import models.Article;
import models.Profile;
import repositories.common.AbstractRepository;

public class ArticleService extends AbstractService<Article> {


    public ArticleService(AbstractRepository<Article, Long> articleRepository) {
        super(articleRepository);
    }

    public Article createArticle(String title, String contents, Profile profile) throws Exception {
        Article article = new Article(title, contents, profile);
        article.checkValidity();
        this.repository.save(article);
        return article;
    }

}
