package models;

public class Profile extends AbstractElement {

    private String login;
    private String fullname;

    public Profile(long id, String login, String fullname) {
        super(id);
        this.login = login;
        this.fullname = fullname;
    }

    public Profile(String login, String fullname) {
        super();
        this.login = login;
        this.fullname = fullname;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @Override
    public void checkValidity() {
        if (this.login == null || this.login.isEmpty()) {
            throw new IllegalArgumentException("Login cannot be empty");
        }
        if (this.login.length() < 3 || this.login.length() > 20) {
            throw new IllegalArgumentException("Login must be between 3 and 20 characters");
        }
        if (this.fullname == null || this.fullname.isEmpty()) {
            throw new IllegalArgumentException("Fullname cannot be empty");
        }
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", fullname='" + fullname + '\'' +
                '}';
    }
}
