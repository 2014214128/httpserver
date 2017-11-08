package com.zg.handler;

import java.util.ArrayList;
import java.util.List;

public class Mapping {
    private String name;
    private List<String> urlPattern;
    Mapping() {
        urlPattern = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<String> getUrlPattern() {
        return urlPattern;
    }

    void setName(String name) {
        this.name = name;
    }

}
