package edu.ucdenver.company;

public abstract class User {
    private String displayName;
    private String email;
    private String password;


    //private final String ACCESS_LEVEL;
    //I changed this because I got errors when using final, final has to be initialized in constructor.
    //instead I put ACCESS_LEVEL in the child classes.

    private String accessLevel;

    public User(String displayName, String email, String password) {
        this.displayName = displayName;
        this.email = email;
        this.password = password;
    }

    public abstract String getAccessLevel();
}
