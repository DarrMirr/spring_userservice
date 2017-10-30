package com.github.darrmirr.response.errors;

/*
 * @author Darr Mirr
 */

public class ErrorInfo {

    private String name;
    private String description;

    protected ErrorInfo() {
    }

    public ErrorInfo(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
