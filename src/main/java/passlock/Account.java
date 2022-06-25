package passlock;

import java.io.Serializable;

public class Account implements Serializable {
    private String sitename;
    private String username;
    private String password;

    public Account(String sitename, String username, String password) {
        this.sitename = sitename;
        this.username = username;
        this.password = password;
    }

    public String getSitename() {
        return this.sitename;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }
}
