package com.kingston.jforgame.admin.utils;

public class SimplyReply {
    private String status;
    
    private String msg;

    public SimplyReply() {
    }

    public SimplyReply(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public static SimplyReply valueOfOk(String msg) {
        SimplyReply reply = new SimplyReply();
        reply.status = "success";
        reply.msg = msg;
        return reply;
    }

    public static SimplyReply valueOfFail(String msg) {
        SimplyReply reply = new SimplyReply();
        reply.status = "success";
        reply.msg = msg;
        return reply;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
