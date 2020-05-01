package com.example.hdfs.domain;

public class ResultMsg {
    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    private boolean login;

    public String getMsg(String 请登录) {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String msg;
}
