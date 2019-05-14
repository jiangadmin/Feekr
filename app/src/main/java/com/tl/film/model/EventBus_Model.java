package com.tl.film.model;

public class EventBus_Model {
    private String command_1;   //一级指令
    private String command_2;   //二级指令

    public String getCommand_1() {
        return command_1;
    }

    public void setCommand_1(String command_1) {
        this.command_1 = command_1;
    }

    public String getCommand_2() {
        return command_2;
    }

    public void setCommand_2(String command_2) {
        this.command_2 = command_2;
    }
}
