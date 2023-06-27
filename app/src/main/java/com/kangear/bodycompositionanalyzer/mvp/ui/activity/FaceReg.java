package com.kangear.bodycompositionanalyzer.mvp.ui.activity;

import java.util.List;

public class FaceReg {
    /**
     * ret : 0
     * msg : ok
     * data : {"group_size":2,"time_ms":347,"candidates":[{"person_id":"201805110001","face_id":"3069590018527882244","confidence":100,"tag":""},{"person_id":"201805110002","face_id":"3069594028536265804","confidence":100,"tag":""}]}
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
         * group_size : 2
         * time_ms : 347
         * candidates : [{"person_id":"201805110001","face_id":"3069590018527882244","confidence":100,"tag":""},{"person_id":"201805110002","face_id":"3069594028536265804","confidence":100,"tag":""}]
         */

        private int group_size;
        private int time_ms;
        private List<CandidatesBean> candidates;

        public int getGroup_size() {
            return group_size;
        }

        public void setGroup_size(int group_size) {
            this.group_size = group_size;
        }

        public int getTime_ms() {
            return time_ms;
        }

        public void setTime_ms(int time_ms) {
            this.time_ms = time_ms;
        }

        public List<CandidatesBean> getCandidates() {
            return candidates;
        }

        public void setCandidates(List<CandidatesBean> candidates) {
            this.candidates = candidates;
        }

        public static class CandidatesBean {
            /**
             * person_id : 201805110001
             * face_id : 3069590018527882244
             * confidence : 100
             * tag :
             */

            private String person_id;
            private String face_id;
            private int confidence;
            private String tag;

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

            public int getConfidence() {
                return confidence;
            }

            public void setConfidence(int confidence) {
                this.confidence = confidence;
            }

            public String getTag() {
                return tag;
            }

            public void setTag(String tag) {
                this.tag = tag;
            }
        }
    }
}
