package com.tl.film.model;

/**
 * @author jiangyao
 * date: 2019/4/17
 * Email: www.fangmu@qq.com
 * Phone: 186 6120 1018
 * TODO:
 */
public class DefTheme_Model extends Base_Model {

    /**
     * code : 1000
     * data : {"bgUrl":"http://testresource.feekrs.com/o_1csqhd69ueub1pal1l5ee85188a7.jpg","cpScan":"","createAuthor":"系统管理员","createTime":"2019-04-03 14:04:39","id":1,"isDelete":0,"logo":"","remark":"","status":1,"themeName":"默认主题","updateAuthor":"系统管理员","updateTime":"2019-04-03 14:04:39"}
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
         * bgUrl : http://testresource.feekrs.com/o_1csqhd69ueub1pal1l5ee85188a7.jpg
         * cpScan :
         * createAuthor : 系统管理员
         * createTime : 2019-04-03 14:04:39
         * id : 1
         * isDelete : 0
         * logo :
         * remark :
         * status : 1
         * themeName : 默认主题
         * updateAuthor : 系统管理员
         * updateTime : 2019-04-03 14:04:39
         */

        private String bgUrl;
        private String cpScan;
        private String createAuthor;
        private String createTime;
        private int id;
        private int isDelete;
        private String logo;
        private String remark;
        private int status;
        private String themeName;
        private String updateAuthor;
        private String updateTime;

        public String getBgUrl() {
            return bgUrl;
        }

        public void setBgUrl(String bgUrl) {
            this.bgUrl = bgUrl;
        }

        public String getCpScan() {
            return cpScan;
        }

        public void setCpScan(String cpScan) {
            this.cpScan = cpScan;
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

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getThemeName() {
            return themeName;
        }

        public void setThemeName(String themeName) {
            this.themeName = themeName;
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
}
