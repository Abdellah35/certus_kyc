package com.softedge.solution.contractmodels;


import java.util.HashMap;
import java.util.Map;

public class MapperObjectCM<String, T extends Object & Comparable<T>> {

    private Map<String, T> objectMapper = new HashMap();

    public MapperObjectCM(Map<String, T> mapper) {
        this.objectMapper = mapper;
    }

    public MapperObjectCM() {
    }

    public Map<String, T> getMapper() {
        return objectMapper;
    }

    public void setMapper(Map<String, T> mapper) {
        this.objectMapper = mapper;
    }
}
