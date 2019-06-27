package com.tl.film.model;

public class Info_Model extends Base_Model {

    /**
     * data : {"merchant":{"areaCode":"310110","areaName":"杨浦区","cityCode":"310100","cityName":"上海市","contacts":"胡欢","createAuthor":"系统管理员","createTime":"2019-05-29 15:22:26","id":41,"isDelete":0,"jumpAction":1,"merchantCode":"100040","merchantName":"胡欢测试测试测试测试测试测试测试","officeAddr":"镇江","phone":"18521308227","provCode":"310000","provName":"上海","remarks":"测试用","status":1,"type":1,"updateAuthor":"胡欢","updateTime":"2019-06-03 22:19:04"},"terminal":{"alias":"","build":1,"createAuthor":"system","createTime":"2019-05-05 21:36:36","guid":"","id":11,"ip":"122.96.99.79","isDelete":0,"mac":"30:88:41:27:AE:D5","mainBoard":"synsepalum_YN","mde":"XGIMI TV","merchantCode":"100040","merchantId":41,"merchantName":"胡欢测试测试测试测试测试测试测试","mertGroupId":0,"mertGroupName":"","tlid":"01F38557B651E9715A5D90284A6759A0","updateAuthor":"system","updateTime":"2019-05-05 21:36:36","useStatus":1,"ver":"1.0.0"}}
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
         * merchant : {"areaCode":"310110","areaName":"杨浦区","cityCode":"310100","cityName":"上海市","contacts":"胡欢","createAuthor":"系统管理员","createTime":"2019-05-29 15:22:26","id":41,"isDelete":0,"jumpAction":1,"merchantCode":"100040","merchantName":"胡欢测试测试测试测试测试测试测试","officeAddr":"镇江","phone":"18521308227","provCode":"310000","provName":"上海","remarks":"测试用","status":1,"type":1,"updateAuthor":"胡欢","updateTime":"2019-06-03 22:19:04"}
         * terminal : {"alias":"","build":1,"createAuthor":"system","createTime":"2019-05-05 21:36:36","guid":"","id":11,"ip":"122.96.99.79","isDelete":0,"mac":"30:88:41:27:AE:D5","mainBoard":"synsepalum_YN","mde":"XGIMI TV","merchantCode":"100040","merchantId":41,"merchantName":"胡欢测试测试测试测试测试测试测试","mertGroupId":0,"mertGroupName":"","tlid":"01F38557B651E9715A5D90284A6759A0","updateAuthor":"system","updateTime":"2019-05-05 21:36:36","useStatus":1,"ver":"1.0.0"}
         */

        private MerchantBean merchant;
        private TerminalBean terminal;

        public MerchantBean getMerchant() {
            return merchant;
        }

        public void setMerchant(MerchantBean merchant) {
            this.merchant = merchant;
        }

        public TerminalBean getTerminal() {
            return terminal;
        }

        public void setTerminal(TerminalBean terminal) {
            this.terminal = terminal;
        }

        public static class MerchantBean {
            /**
             * areaCode : 310110
             * areaName : 杨浦区
             * cityCode : 310100
             * cityName : 上海市
             * contacts : 胡欢
             * createAuthor : 系统管理员
             * createTime : 2019-05-29 15:22:26
             * id : 41
             * isDelete : 0
             * jumpAction : 1
             * merchantCode : 100040
             * merchantName : 胡欢测试测试测试测试测试测试测试
             * officeAddr : 镇江
             * phone : 18521308227
             * provCode : 310000
             * provName : 上海
             * remarks : 测试用
             * status : 1
             * type : 1
             * updateAuthor : 胡欢
             * updateTime : 2019-06-03 22:19:04
             */

            private String areaCode;
            private String areaName;
            private String cityCode;
            private String cityName;
            private String contacts;
            private String createAuthor;
            private String createTime;
            private int id;
            private int isDelete;
            private int jumpAction;
            private String merchantCode;
            private String merchantName;
            private String officeAddr;
            private String phone;
            private String provCode;
            private String provName;
            private String remarks;
            private int status;
            private int type;
            private String updateAuthor;
            private String updateTime;

            public String getAreaCode() {
                return areaCode;
            }

            public void setAreaCode(String areaCode) {
                this.areaCode = areaCode;
            }

            public String getAreaName() {
                return areaName;
            }

            public void setAreaName(String areaName) {
                this.areaName = areaName;
            }

            public String getCityCode() {
                return cityCode;
            }

            public void setCityCode(String cityCode) {
                this.cityCode = cityCode;
            }

            public String getCityName() {
                return cityName;
            }

            public void setCityName(String cityName) {
                this.cityName = cityName;
            }

            public String getContacts() {
                return contacts;
            }

            public void setContacts(String contacts) {
                this.contacts = contacts;
            }

            public String getCreateAuthor() {
                return createAuthor;
            }

            public void setCreateAuthor(String createAuthor) {
                this.createAuthor = createAuthor;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getIsDelete() {
                return isDelete;
            }

            public void setIsDelete(int isDelete) {
                this.isDelete = isDelete;
            }

            public int getJumpAction() {
                return jumpAction;
            }

            public void setJumpAction(int jumpAction) {
                this.jumpAction = jumpAction;
            }

            public String getMerchantCode() {
                return merchantCode;
            }

            public void setMerchantCode(String merchantCode) {
                this.merchantCode = merchantCode;
            }

            public String getMerchantName() {
                return merchantName;
            }

            public void setMerchantName(String merchantName) {
                this.merchantName = merchantName;
            }

            public String getOfficeAddr() {
                return officeAddr;
            }

            public void setOfficeAddr(String officeAddr) {
                this.officeAddr = officeAddr;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getProvCode() {
                return provCode;
            }

            public void setProvCode(String provCode) {
                this.provCode = provCode;
            }

            public String getProvName() {
                return provName;
            }

            public void setProvName(String provName) {
                this.provName = provName;
            }

            public String getRemarks() {
                return remarks;
            }

            public void setRemarks(String remarks) {
                this.remarks = remarks;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getUpdateAuthor() {
                return updateAuthor;
            }

            public void setUpdateAuthor(String updateAuthor) {
                this.updateAuthor = updateAuthor;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }
        }

        public static class TerminalBean {
            /**
             * alias :
             * build : 1
             * createAuthor : system
             * createTime : 2019-05-05 21:36:36
             * guid :
             * id : 11
             * ip : 122.96.99.79
             * isDelete : 0
             * mac : 30:88:41:27:AE:D5
             * mainBoard : synsepalum_YN
             * mde : XGIMI TV
             * merchantCode : 100040
             * merchantId : 41
             * merchantName : 胡欢测试测试测试测试测试测试测试
             * mertGroupId : 0
             * mertGroupName :
             * tlid : 01F38557B651E9715A5D90284A6759A0
             * updateAuthor : system
             * updateTime : 2019-05-05 21:36:36
             * useStatus : 1
             * ver : 1.0.0
             */

            private String alias;
            private int build;
            private String createAuthor;
            private String createTime;
            private String guid;
            private int id;
            private String ip;
            private int isDelete;
            private String mac;
            private String mainBoard;
            private String mde;
            private String merchantCode;
            private int merchantId;
            private String merchantName;
            private int mertGroupId;
            private String mertGroupName;
            private String tlid;
            private String updateAuthor;
            private String updateTime;
            private int useStatus;
            private String ver;

            public String getAlias() {
                return alias;
            }

            public void setAlias(String alias) {
                this.alias = alias;
            }

            public int getBuild() {
                return build;
            }

            public void setBuild(int build) {
                this.build = build;
            }

            public String getCreateAuthor() {
                return createAuthor;
            }

            public void setCreateAuthor(String createAuthor) {
                this.createAuthor = createAuthor;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getGuid() {
                return guid;
            }

            public void setGuid(String guid) {
                this.guid = guid;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getIp() {
                return ip;
            }

            public void setIp(String ip) {
                this.ip = ip;
            }

            public int getIsDelete() {
                return isDelete;
            }

            public void setIsDelete(int isDelete) {
                this.isDelete = isDelete;
            }

            public String getMac() {
                return mac;
            }

            public void setMac(String mac) {
                this.mac = mac;
            }

            public String getMainBoard() {
                return mainBoard;
            }

            public void setMainBoard(String mainBoard) {
                this.mainBoard = mainBoard;
            }

            public String getMde() {
                return mde;
            }

            public void setMde(String mde) {
                this.mde = mde;
            }

            public String getMerchantCode() {
                return merchantCode;
            }

            public void setMerchantCode(String merchantCode) {
                this.merchantCode = merchantCode;
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

            public int getMertGroupId() {
                return mertGroupId;
            }

            public void setMertGroupId(int mertGroupId) {
                this.mertGroupId = mertGroupId;
            }

            public String getMertGroupName() {
                return mertGroupName;
            }

            public void setMertGroupName(String mertGroupName) {
                this.mertGroupName = mertGroupName;
            }

            public String getTlid() {
                return tlid;
            }

            public void setTlid(String tlid) {
                this.tlid = tlid;
            }

            public String getUpdateAuthor() {
                return updateAuthor;
            }

            public void setUpdateAuthor(String updateAuthor) {
                this.updateAuthor = updateAuthor;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }

            public int getUseStatus() {
                return useStatus;
            }

            public void setUseStatus(int useStatus) {
                this.useStatus = useStatus;
            }

            public String getVer() {
                return ver;
            }

            public void setVer(String ver) {
                this.ver = ver;
            }
        }
    }
}
