package com.example.finalprojectgamebook.model;

public class Section {
    private String name;
    private String type;
    private String description;

    public Section(){}

    public Section(String name,String type,String description){
        this.name = name;
        this.type = type;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

}
