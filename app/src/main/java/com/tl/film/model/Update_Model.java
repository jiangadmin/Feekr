package com.tl.film.model;

/**
 * Created by  jiang
 * on 2017/7/16.
 * Email: www.fangmu@qq.com
 * Phone：186 6120 1018
 * Purpose:TODO 更新
 * update：
 */
public class Update_Model extends Base_Model {

    /**
     * code : 1000
     * data : {"build":104,"createAuthor":"系统管理员","createTime":"2019-05-09 17:49:58","downloadUrl":"aa","id":4,"isDelete":0,"remark":"","updateAuthor":"系统管理员","updateTime":"2019-05-10 17:41:38","useStatus":1,"ver":"Version 1.0.4"}
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
         * build : 104
         * createAuthor : 系统管理员
         * createTime : 2019-05-09 17:49:58
         * downloadUrl : aa
         * id : 4
         * isDelete : 0
         * remark :
         * updateAuthor : 系统管理员
         * updateTime : 2019-05-10 17:41:38
         * useStatus : 1
         * ver : Version 1.0.4
         */

        private int build;
        private String createAuthor;
        private String createTime;
        private String downloadUrl;
        private int id;
        private int isDelete;
        private String remark;
        private String updateAuthor;
        private String updateTime;
        private int useStatus;
        private String ver;

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

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public void setDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
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

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
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
