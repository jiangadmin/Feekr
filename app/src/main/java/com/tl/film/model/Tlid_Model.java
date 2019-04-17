package com.tl.film.model;

/**
 * @author jiangyao
 * date: 2019/4/17
 * Email: www.fangmu@qq.com
 * Phone: 186 6120 1018
 * TODO:
 */
public class Tlid_Model extends Base_Model {

    /**
     * data : {"tlid":"123asdfa123qeawrwaeradwfssa"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * tlid : 123asdfa123qeawrwaeradwfssa
         */

        private String tlid;

        public String getTlid() {
            return tlid;
        }

        public void setTlid(String tlid) {
            this.tlid = tlid;
        }
    }
}
