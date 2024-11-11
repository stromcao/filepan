package com.filepan.entity;

public class Role {
    private int id;
    private String role_name;
    private String role_group;

    public Role() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public String getRole_group() {
        return role_group;
    }

    public void setRole_group(String role_group) {
        this.role_group = role_group;
    }
}
