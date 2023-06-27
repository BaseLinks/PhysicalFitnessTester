package com.kangear.bodycompositionanalyzer.mvp.ui.activity;

import java.util.List;

public class NewPerson {

    /**
     * ret : 0
     * msg : ok
     * data : {"suc_group":1,"suc_face":1,"person_id":"1555585090831","face_id":"3069846174704869885","group_ids":["group20180511"]}
     */

    private int ret;
    private String msg;
    private DataBean data;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * suc_group : 1
         * suc_face : 1
         * person_id : 1555585090831
         * face_id : 3069846174704869885
         * group_ids : ["group20180511"]
         */

        private int suc_group;
        private int suc_face;
        private String person_id;
        private String face_id;
        private List<String> group_ids;

        public int getSuc_group() {
            return suc_group;
        }

        public void setSuc_group(int suc_group) {
            this.suc_group = suc_group;
        }

        public int getSuc_face() {
            return suc_face;
        }

        public void setSuc_face(int suc_face) {
            this.suc_face = suc_face;
        }

        public String getPerson_id() {
            return person_id;
        }

        public void setPerson_id(String person_id) {
            this.person_id = person_id;
        }

        public String getFace_id() {
            return face_id;
        }

        public void setFace_id(String face_id) {
            this.face_id = face_id;
        }

        public List<String> getGroup_ids() {
            return group_ids;
        }

        public void setGroup_ids(List<String> group_ids) {
            this.group_ids = group_ids;
        }
    }
}
