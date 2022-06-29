package passlock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MasterAccount implements Serializable {
    private List<Account> accounts = new ArrayList<Account>();
    private String hashedPassword;

    public MasterAccount(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public MasterAccount() {

    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getHashedPassword() {
        return this.hashedPassword;
    }

    public List<Account> getAccounts() {
        return this.accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}
