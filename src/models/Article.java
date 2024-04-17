package models;

public class Article extends AbstractElement {

    private long id;
    private String title;
    private String content;
    private Profile author;

    public Article(long id, String title, String content, Profile author) {
        super(id);
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public Article(String title, String content, Profile author) {
        super();
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public long getId() {
        return id;
    }

    public void setId(long newId) {
        this.id = newId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Profile getAuthor() {
        return author;
    }

    @Override
    public void checkValidity() throws Exception {
        if (this.title == null || this.title.isEmpty()) {
            throw new Exception("Title cannot be empty");
        }
        if (this.content == null || this.content.isEmpty()) {
            throw new Exception("Content cannot be empty");
        }
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", author=" + author +
                '}';
    }
}
