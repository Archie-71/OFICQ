public class UserData {
    boolean userNameExist = false;
    boolean loginSuccess = false;

    private final String[] userDefault = {
            "admin", "admin"
    };
    private String[] user1 = {
            "Archie", "12345678"
    };

    public UserData() {
    }

    protected boolean isUserNameExist(String username) {
        return username.equals(userDefault[0]) | username.equals(user1[0]);
    }

    protected boolean isLoginSuccess(String password) {
        return password.equals(userDefault[1]) | password.equals(user1[1]);
    }
}