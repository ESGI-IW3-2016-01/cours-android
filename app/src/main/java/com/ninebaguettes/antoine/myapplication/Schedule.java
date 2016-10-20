package com.ninebaguettes.antoine.myapplication;

import java.util.HashMap;

/**
 * Created by Antoine on 20/10/2016.
 */

public class Schedule {

    private String destination;
    private String message;

    public Schedule(String destination, String message) {
        this.destination = destination;
        this.message = message;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "destination='" + destination + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public HashMap<String, String> toHashMap() {
        HashMap<String, String> tableau = new HashMap<>();
        tableau.put("destination", this.destination);
        tableau.put("message", this.message);

        return tableau;
    }
}
