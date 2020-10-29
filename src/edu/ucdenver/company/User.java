package edu.ucdenver.company;

import java.io.Serializable;

public abstract class User implements Serializable {
    private String displayName;
    private String email;
    private String password;
    private boolean loggedIn;

    private String accessLevel;

    public User(String displayName, String email, String password) {
        this.displayName = displayName;
        this.email = email;
        this.password = password;
        this.loggedIn = false;
    }

    public abstract String getAccessLevel();

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    @Override
    public String toString() {
        return String.format("Name:%s Email:%s Access Level:%s ", displayName, email, this.getAccessLevel());
    }
}
