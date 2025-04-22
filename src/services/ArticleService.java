package services;

import models.Article;
import models.Profile;
import repositories.common.AbstractRepository;
import repositories.common.ArticleRepository;

public class ArticleService extends AbstractService<Article> {


    public ArticleService(ArticleRepository articleRepository) {
        super(articleRepository);
    }

    public Article createArticle(String title, String contents, Profile profile) throws Exception {
        Article article = new Article(title, contents, profile);
        article.checkValidity();
        this.repository.save(article);
        return article;
    }

}
