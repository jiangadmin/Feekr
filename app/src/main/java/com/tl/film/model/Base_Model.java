package com.tl.film.model;

/**
 * @author jiangyao
 * date: 2019/3/22
 * Email: www.fangmu@qq.com
 * Phone: 186 6120 1018
 * TODO:
 */
public class Base_Model {

    /**
     * code : 0015
     * message : 请求数据不存在！
     * data : null
     */

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        switch (code) {
            case -1:
                return "连接服务器失败";
            case -2:
                return "数据解析失败";
        }
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
