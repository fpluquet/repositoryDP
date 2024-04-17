package repositories.memory;

import models.Article;
import repositories.filters.AbstractFilter;
import repositories.filters.visitors.FilterEvaluator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArticleRepository extends repositories.ArticleRepository {

    List<Article> articles = new ArrayList<>();

    @Override
    public List<Article> findAll() {
        return articles;
    }

    @Override
    public Article getById(Long aLong) throws IOException {
        return articles.stream().filter(article -> article.getId() == aLong).findFirst().orElseThrow();
    }

    @Override
    public void save(Article article) throws IOException {
        if (this.exists(article.getId())) {
            this.update(article);
        } else {
            articles.add(article);
        }
    }

    private boolean exists(long id) {
        return articles.stream().anyMatch(a -> a.getId() == id);
    }

    @Override
    public void update(Article article) throws IOException {
        if (articles.stream().noneMatch(a -> a.getId() == article.getId())) {
            throw new IOException("Article not found");
        }
        final Article existingArticle = articles.stream().filter(a -> a.getId() == article.getId()).findFirst().get();
        existingArticle.setTitle(article.getTitle());
        existingArticle.setContent(article.getContent());
    }

    @Override
    public void delete(Article article) throws IOException {
        if (articles.stream().filter(a -> a.getId() == article.getId()).count() == 0) {
            throw new IOException("Article not found");
        }
        articles = articles.stream().filter(a -> a.getId() != article.getId()).toList();
    }

    @Override
    public Article get(AbstractFilter<Article> filter) throws Exception {
        return this.get(a -> FilterEvaluator.match(a, filter));
    }

    @Override
    public List<Article> getAll(AbstractFilter<Article> filter) throws Exception {
        return this.getAll(a -> FilterEvaluator.match(a, filter));
    }

}
