package passlock;

import java.io.Serializable;

public class Master implements Serializable {
    private String hasdhedPassword;

    public Master(String hasdhedPassword) {
        this.hasdhedPassword = hasdhedPassword;
    }

    public String getHasdhedPassword() {
        return hasdhedPassword;
    }
}
