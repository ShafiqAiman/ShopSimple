package com.example.shopsimple1.Model;

public class User {

    public User() {
    }

    public User(String name, String password) {
        Name = name;
        Password = password;
    }

    private String Name;
    private String Password;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

}
