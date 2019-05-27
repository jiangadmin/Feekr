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
     * data : {"merchantCode":"","tlid":"d3ae3dd146b04e769ca3f51730881eee","merchantId":0,"merchantName":""}
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
         * merchantCode :
         * tlid : d3ae3dd146b04e769ca3f51730881eee
         * merchantId : 0
         * merchantName :
         */

        private String merchantCode;
        private String tlid;
        private int merchantId;
        private String merchantName;

        public String getMerchantCode() {
            return merchantCode;
        }

        public void setMerchantCode(String merchantCode) {
            this.merchantCode = merchantCode;
        }

        public String getTlid() {
            return tlid;
        }

        public void setTlid(String tlid) {
            this.tlid = tlid;
        }

        public int getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(int merchantId) {
            this.merchantId = merchantId;
        }

        public String getMerchantName() {
            return merchantName;
        }

        public void setMerchantName(String merchantName) {
            this.merchantName = merchantName;
        }
    }
}
