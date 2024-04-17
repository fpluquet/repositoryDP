package repositories.files;

import models.Article;
import models.Profile;
import repositories.SearchCriteria;
import repositories.filters.AbstractFilter;
import repositories.filters.visitors.FilterEvaluator;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ArticleRepository extends repositories.ArticleRepository {

    private final FilesRepositoryFactory repositoryFactory;

    private final FileLinesStream fileLinesStream;

    private int nextId = 1;

    public ArticleRepository(FilesRepositoryFactory repositoryFactory, String folder) throws IOException {
        super();
        this.repositoryFactory = repositoryFactory;
        this.fileLinesStream = new FileLinesStream(Path.of(folder, "articles.txt"));
    }

    public List<String> articleToLines(Article article) {
        List<String> lines = new ArrayList<String>();
        lines.add("ID: " + article.getId());
        lines.add("Title: " + article.getTitle());
        lines.add("Content: " + article.getContent());
        lines.add("Author: " + article.getAuthor().getId());
        return lines;
    }

    public Article articleFromStream(FileLinesStream stream) throws Exception {
        final long id = Long.parseLong(stream.nextLine().split(": ")[1]);
        final String title = stream.nextLine().split(": ")[1];
        final String content = stream.nextLine().split(": ")[1];
        final long authorId = Long.parseLong(stream.nextLine().split(": ")[1]);
        final Profile author = repositoryFactory.getProfileRepository().getById(authorId);
        return new Article(id, title, content, author);
    }

    @Override
    public List<Article> findAll() throws Exception {
        fileLinesStream.reset();
        List<Article> articles = new ArrayList<>();
        while (fileLinesStream.hasNext()) {
            Article article = articleFromStream(fileLinesStream);
            articles.add(article);
            if (article.getId() >= nextId)
                nextId = Math.toIntExact(article.getId() + 1);
        }
        return articles;
    }


    @Override
    public Article getById(Long aLong) throws Exception {
        return this.findAll().stream().filter(article -> article.getId() == aLong).findFirst().orElseThrow();
    }

    @Override
    public void save(Article article) throws Exception {
        List<Article> articles = this.findAll();
        articles.add(article);
        article.setId(nextId++);
        writeAll(articles);
    }

    @Override
    public void update(Article article) throws Exception {
        List<Article> articles = this.findAll();
        articles = new ArrayList<>(articles.stream().filter(a -> a.getId() != article.getId()).toList());
        articles.add(article);
        writeAll(articles);
    }

    @Override
    public void delete(Article article) throws Exception {
        List<Article> articles = this.findAll();
        articles = articles.stream().filter(a -> a.getId() != article.getId()).toList();
        writeAll(articles);
    }

    @Override
    public Article get(SearchCriteria<Article> criteria) throws Exception {
        return this.findAll().stream().filter(criteria::match).findFirst().orElseThrow();
    }

    @Override
    public List<Article> getAll(SearchCriteria<Article> criteria) throws Exception {
        return this.findAll().stream().filter(criteria::match).toList();
    }

    @Override
    public Article get(AbstractFilter<Article> filter) throws Exception {
        return this.get(a -> FilterEvaluator.match(a, filter));
    }

    @Override
    public List<Article> getAll(AbstractFilter<Article> filter) throws Exception {
        return this.getAll(a -> FilterEvaluator.match(a, filter));
    }

    private void writeAll(List<Article> articles) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Article article : articles) {
            lines.addAll(articleToLines(article));
        }
        fileLinesStream.writeLines(lines);
    }
}
