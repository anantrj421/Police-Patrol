package com.example.ebeat;

public class checkinout {
    String name;
    String timein;
    String timeout;

    public checkinout(String name, String timein, String timeout) {
        this.name = name;
        this.timein = timein;
        this.timeout = timeout;
    }
    public checkinout(){}

    public String getName() {
        return name;
    }

    public String getTimein() {
        return timein;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimein(String timein) {
        this.timein = timein;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }
}
