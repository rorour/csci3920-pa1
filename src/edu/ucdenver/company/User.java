/** PA1
 * Raven O'Rourke & Lora Kalthoff
 *
 * User Abstract class:
 * Parent class for Administrator and Customer class
 */
package edu.ucdenver.company;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class User implements Serializable {
    private String displayName;
    private String email;
    private String password;
    private boolean loggedIn;

    private String accessLevel;

    public User(String displayName, String email, String password) {
        this.displayName = displayName;
        setEmail(email);
        this.loggedIn = false;
        setPassword(password);
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

    public void setEmail(String email) throws IllegalArgumentException {
        if (email.length() < 5){
            throw new IllegalArgumentException("Invalid email address - too short");
        }
        String[] emailSplit = email.split("@");
        if (emailSplit.length != 2){
            throw new IllegalArgumentException("Invalid email address - cannot find @ symbol");
        }
        String after_at_symbol = emailSplit[1];
        if (after_at_symbol.length() < 3){
            throw new IllegalArgumentException("Invalid email address - domain too short");
        }
        String[] after_at_split = after_at_symbol.split("\\.");
        if (after_at_split.length != 2){
            throw new IllegalArgumentException("Invalid email address - domain should be 2 words separated by dots");
        }
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password.length() < 8){
            throw new IllegalArgumentException("Password must be at least 8 characters.");
        } else {
            this.password = password;
        }
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
