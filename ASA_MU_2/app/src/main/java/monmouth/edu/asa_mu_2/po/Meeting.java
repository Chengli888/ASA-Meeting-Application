package monmouth.edu.asa_mu_2.po;

import java.util.List;

/**
 * Created by chengli on 2017/11/5.
 */


    /**
     * Copyright 2017 bejson.com
     */
    /**
     * Auto-generated: 2017-11-05 15:40:45
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */
    public class Meeting {

        private String DATE;
        private String TIME;
        private String SESSION;
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setDATE(String DATE) {
            this.DATE = DATE;
        }
        public String getDATE() {
            return DATE;
        }

        public void setTIME(String TIME) {
            this.TIME = TIME;
        }
        public String getTIME() {
            return TIME;
        }

        public void setSESSION(String SESSION) {
            this.SESSION = SESSION;
        }
        public String getSESSION() {
            return SESSION;
        }

    }

