package com.softedge.solution.contractmodels;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.util.HashMap;
import java.util.Map;

public class MessageTextCM {

    private String text;
    private String className;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @SneakyThrows
    @Override
    public String toString() {
        ObjectMapper Obj = new ObjectMapper();
        String jsonStr = Obj.writeValueAsString(this);
        return jsonStr;
    }
}
