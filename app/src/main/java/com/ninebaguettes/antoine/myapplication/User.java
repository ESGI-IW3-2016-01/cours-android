package com.ninebaguettes.antoine.myapplication;

import java.util.HashMap;

public class User {

    private String firtName;
    private String lastName;
    private String email;

    public String getFirtName() {
        return firtName;
    }

    public void setFirtName(String firtName) {
        this.firtName = firtName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User(String firtName, String lastName, String email) {

        this.firtName = firtName;
        this.lastName = lastName;
        this.email = email;
    }

    public HashMap<String, String> toHashMap() {
        HashMap<String, String> tableau = new HashMap<>();
        tableau.put("name", this.firtName + " " + this.lastName);
        tableau.put("email", this.email);

        return tableau;
    }
}
