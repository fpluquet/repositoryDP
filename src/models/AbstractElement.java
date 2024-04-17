package models;

public class AbstractElement {

    private final long NoID = -1;
    protected long id;

    public AbstractElement(long id) {
        this.id = id;
    }

    public AbstractElement() {
        this.id = NoID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isSaved() {
        return this.id != NoID;
    }

    public void checkValidity() throws Exception {
    }
}
