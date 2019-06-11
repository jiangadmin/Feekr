package com.tl.film.model;

public class Push_Model {

    /**
     * data : {"tlid":"8ff838f1306a41eabbf3c381eed7d633"}
     * eventId : OPEN_TX_FILM
     */

    private DataBean data;
    private String eventId;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public static class DataBean {
        /**
         * tlid : 8ff838f1306a41eabbf3c381eed7d633
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
