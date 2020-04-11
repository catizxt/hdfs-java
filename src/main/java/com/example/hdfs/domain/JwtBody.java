package com.example.hdfs.domain;

import java.util.List;

public class JwtBody {
    private String method;

    public List<String> getArgs() {
        return args;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }

    private List<String> args;



    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }


}
