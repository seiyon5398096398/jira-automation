package com.sample.demo.models;


import java.util.Map;

public class IssueRequest {
    private Map<String, Object> fields;

    public Map<String, Object> getFields() {
        return fields;
    }

    public void setFields(Map<String, Object> fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return "IssueRequest{" +
                "fields=" + fields +
                '}';
    }
}
