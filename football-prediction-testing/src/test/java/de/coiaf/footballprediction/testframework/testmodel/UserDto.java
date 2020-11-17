package de.coiaf.footballprediction.testframework.testmodel;

import java.util.ArrayList;
import java.util.List;

public class UserDto {

    private static final long serialVersionUID = 1L;
    private long id;
    private String username;
    private String firstName;
    private String lastName;

    private final List<String> telephones = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String login) {
        this.username = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<String> getTelephones() {
        return telephones;
    }
}
