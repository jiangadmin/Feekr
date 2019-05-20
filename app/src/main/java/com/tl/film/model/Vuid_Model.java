package com.tl.film.model;

public class Vuid_Model extends Base_Model {

    /**
     * code : 1000
     * data : {"accessToken":"wTtjRr0y+Cct74S5ysrJ570RtZm1FWC2+ZwYRNAonyGyaJUe5DuaBIrD7qpYaCTaQcEk9qJsdc2DoIwpMydWKUHT7itxzjLQ","vuid":"1810000326","vtoken":"02C2E7CD0B0B7359281D1ED89CA9E98B"}
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
         * accessToken : wTtjRr0y+Cct74S5ysrJ570RtZm1FWC2+ZwYRNAonyGyaJUe5DuaBIrD7qpYaCTaQcEk9qJsdc2DoIwpMydWKUHT7itxzjLQ
         * vuid : 1810000326
         * vtoken : 02C2E7CD0B0B7359281D1ED89CA9E98B
         */

        private String accessToken;
        private long vuid;
        private String vtoken;

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public long getVuid() {
            return vuid;
        }

        public void setVuid(long vuid) {
            this.vuid = vuid;
        }

        public String getVtoken() {
            return vtoken;
        }

        public void setVtoken(String vtoken) {
            this.vtoken = vtoken;
        }
    }
}
