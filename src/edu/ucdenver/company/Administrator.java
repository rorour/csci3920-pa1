package edu.ucdenver.company;

import java.lang.reflect.AccessibleObject;

public class Administrator extends User {
    private final String ACCESS_LEVEL;
    public Administrator(String displayName, String email, String password) {
        super(displayName, email, password);
        this.ACCESS_LEVEL = "admin";

    }

    @Override
    public String getAccessLevel() {
        return ACCESS_LEVEL;
    }
}
