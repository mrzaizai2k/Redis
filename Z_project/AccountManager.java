import java.util.ArrayList;
import java.util.List;

public class AccountManager {
    private List<UserAccount> userAccounts;

    public AccountManager() {
        userAccounts = new ArrayList<>();
    }

    public void addUser(UserAccount user) {
        userAccounts.add(user);
    }

    public UserAccount getUserByUsername(String username) {
        for (UserAccount user : userAccounts) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null; // User not found
    }

    public boolean authenticateUser(String username, String password) {
        UserAccount user = getUserByUsername(username);
        return user != null & user.getPassword().equals(password);
    }


}
