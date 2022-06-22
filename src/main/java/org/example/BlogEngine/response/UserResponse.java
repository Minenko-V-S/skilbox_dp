package org.example.BlogEngine.response;

import org.example.BlogEngine.model.Posts;

public class UserResponse {

    private Integer id;
    private String name;
    private String photo;

    public UserResponse() {
    }

    public UserResponse(Posts post) {
    }

    public UserResponse(Integer id, String name, String photo) {
        this.id = id;
        this.name = name;
        this.photo = photo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
