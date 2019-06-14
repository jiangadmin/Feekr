package com.tl.film.model;

public class EventBus_Model {
    public static final String CMD_BIND_MERT  = "CMD_BIND_MERT";                //绑定商户
    public static final String CMD_BIND_MERT_FAIL = "CMD_BIND_MERT_FAIL";       //绑定商户失败提示
    public static final String CMD_BIND_FAIL = "CMD_BIND_FAIL";                 //设备注册失败提示
    public static final String CMD_ENTRY_HOME = "CMD_ENTRY_HOME";               //打开主界面
    public static final String CMD_FILL_DATA_THEME = "CMD_FILL_DATA_THEME";     //加载主题数据
    public static final String CMD_FILL_DATA_FILM = "CMD_FILL_DATA_FILM";       //加载影片数据
    public static final String CMD_UPGRADE = "CMD_UPGRADE";                     //APP升级
    public static final String CMD_NET_CONNECT = "CMD_NET_CONNECT";             //网络连接

    private String command_1;   //一级指令
    private String command_2;   //二级指令
    private Object data;

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
