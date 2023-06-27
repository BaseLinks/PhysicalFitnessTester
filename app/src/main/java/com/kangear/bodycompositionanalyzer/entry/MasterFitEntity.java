package com.kangear.bodycompositionanalyzer.entry;

import com.kangear.bodycompositionanalyzer.BodyComposition;
import com.kangear.bodycompositionanalyzer.Record;
import com.kangear.bodycompositionanalyzer.application.App;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MasterFitEntity {
    /**
     * sn : 882c1ee687b700000000
     * timestamp : 1627630264030
     * sign : 4db8b81e2be5ab80ac62ee96677113d2
     * records : [{"uid":"18211569185","gender":"0","age":"19","height":"170","weight":"100","leanBody":"90","muscle":"30","basalMetabolism":"1137","bodyAge":"20","bodyMoisture":"22.10","pbf":"28.40","protein":"7.90","mineralSalt":"20.2","raFat":"0.50","laMuscle":"4.20","raMuscle":"3.10","rlFat":"1.40","laFat":"2.0","llFat":"3.1","llMuscle":"30.1","rlMuscle":"6.60","grade":"2","skeletalMuscle":"14.00","totalEnergy":"1695.00","trunkFat":"7.10","trunkMuscle":"15.50","visceralArea":"84.60","whr":"0.87","completeTime":"2021-07-30 15:20:55"}]
     */

    private String sn;
    private String timestamp;
    private String sign;
    private List<RecordsBean> records;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public List<RecordsBean> getRecords() {
        return records;
    }

    public void setRecords(List<RecordsBean> records) {
        this.records = records;
    }

    public static class RecordsBean {
        /**
         * uid : 18211569185
         * gender : 0
         * age : 19
         * height : 170
         * weight : 100
         * leanBody : 90
         * muscle : 30
         * basalMetabolism : 1137
         * bodyAge : 20
         * bodyMoisture : 22.10
         * pbf : 28.40
         * protein : 7.90
         * mineralSalt : 20.2
         * raFat : 0.50
         * laMuscle : 4.20
         * raMuscle : 3.10
         * rlFat : 1.40
         * laFat : 2.0
         * llFat : 3.1
         * llMuscle : 30.1
         * rlMuscle : 6.60
         * grade : 2
         * skeletalMuscle : 14.00
         * totalEnergy : 1695.00
         * trunkFat : 7.10
         * trunkMuscle : 15.50
         * visceralArea : 84.60
         * whr : 0.87
         * completeTime : 2021-07-30 15:20:55
         */

        private String uid;
        private String gender;
        private String age;
        private String height;
        private String weight;
        private String leanBody;
        private String muscle;
        private String basalMetabolism;
        private String bodyAge;
        private String bodyMoisture;
        private String pbf;
        private String protein;
        private String mineralSalt;
        private String raFat;
        private String laMuscle;
        private String raMuscle;
        private String rlFat;
        private String laFat;
        private String llFat;
        private String llMuscle;
        private String rlMuscle;
        private String grade;
        private String skeletalMuscle;
        private String totalEnergy;
        private String trunkFat;
        private String trunkMuscle;
        private String visceralArea;
        private String whr;
        private String completeTime;
        private String bodyFat;
        private String bodyTypeAnalysis;
        private String z50k;
        private String z250k;
        private String z5k;
        private String edemaCoefficient;
        private String extracellularFluid;
        private String intracellularFluid;
        private String bmi;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getLeanBody() {
            return leanBody;
        }

        public void setLeanBody(String leanBody) {
            this.leanBody = leanBody;
        }

        public String getMuscle() {
            return muscle;
        }

        public void setMuscle(String muscle) {
            this.muscle = muscle;
        }

        public String getBasalMetabolism() {
            return basalMetabolism;
        }

        public void setBasalMetabolism(String basalMetabolism) {
            this.basalMetabolism = basalMetabolism;
        }

        public String getBodyAge() {
            return bodyAge;
        }

        public void setBodyAge(String bodyAge) {
            this.bodyAge = bodyAge;
        }

        public String getBodyMoisture() {
            return bodyMoisture;
        }

        public void setBodyMoisture(String bodyMoisture) {
            this.bodyMoisture = bodyMoisture;
        }

        public String getPbf() {
            return pbf;
        }

        public void setPbf(String pbf) {
            this.pbf = pbf;
        }

        public String getProtein() {
            return protein;
        }

        public void setProtein(String protein) {
            this.protein = protein;
        }

        public String getMineralSalt() {
            return mineralSalt;
        }

        public void setMineralSalt(String mineralSalt) {
            this.mineralSalt = mineralSalt;
        }

        public String getRaFat() {
            return raFat;
        }

        public void setRaFat(String raFat) {
            this.raFat = raFat;
        }

        public String getLaMuscle() {
            return laMuscle;
        }

        public void setLaMuscle(String laMuscle) {
            this.laMuscle = laMuscle;
        }

        public String getRaMuscle() {
            return raMuscle;
        }

        public void setRaMuscle(String raMuscle) {
            this.raMuscle = raMuscle;
        }

        public String getRlFat() {
            return rlFat;
        }

        public void setRlFat(String rlFat) {
            this.rlFat = rlFat;
        }

        public String getLaFat() {
            return laFat;
        }

        public void setLaFat(String laFat) {
            this.laFat = laFat;
        }

        public String getLlFat() {
            return llFat;
        }

        public void setLlFat(String llFat) {
            this.llFat = llFat;
        }

        public String getLlMuscle() {
            return llMuscle;
        }

        public void setLlMuscle(String llMuscle) {
            this.llMuscle = llMuscle;
        }

        public String getRlMuscle() {
            return rlMuscle;
        }

        public void setRlMuscle(String rlMuscle) {
            this.rlMuscle = rlMuscle;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getSkeletalMuscle() {
            return skeletalMuscle;
        }

        public void setSkeletalMuscle(String skeletalMuscle) {
            this.skeletalMuscle = skeletalMuscle;
        }

        public String getTotalEnergy() {
            return totalEnergy;
        }

        public void setTotalEnergy(String totalEnergy) {
            this.totalEnergy = totalEnergy;
        }

        public String getTrunkFat() {
            return trunkFat;
        }

        public void setTrunkFat(String trunkFat) {
            this.trunkFat = trunkFat;
        }

        public String getTrunkMuscle() {
            return trunkMuscle;
        }

        public void setTrunkMuscle(String trunkMuscle) {
            this.trunkMuscle = trunkMuscle;
        }

        public String getVisceralArea() {
            return visceralArea;
        }

        public void setVisceralArea(String visceralArea) {
            this.visceralArea = visceralArea;
        }

        public String getWhr() {
            return whr;
        }

        public void setWhr(String whr) {
            this.whr = whr;
        }

        public String getCompleteTime() {
            return completeTime;
        }

        public void setCompleteTime(String completeTime) {
            this.completeTime = completeTime;
        }

        public void setBodyFat(String bodyFat) {
            this.bodyFat = bodyFat;
        }

        public String getBodyFat() {
            return bodyFat;
        }

        public void setBodyTypeAnalysis(String bodyTypeAnalysis) {
            this.bodyTypeAnalysis = bodyTypeAnalysis;
        }

        public String getBodyTypeAnalysis() {
            return bodyTypeAnalysis;
        }

        public void setZ50k(String z50k) {
            this.z50k = z50k;
        }

        public String getZ50k() {
            return z50k;
        }

        public void setZ250k(String z250k) {
            this.z250k = z250k;
        }

        public String getZ250k() {
            return z250k;
        }

        public void setZ5k(String z5k) {
            this.z5k = z5k;
        }

        public String getZ5k() {
            return z5k;
        }

        public void setEdemaCoefficient(String edemaCoefficient) {
            this.edemaCoefficient = edemaCoefficient;
        }

        public String getEdemaCoefficient() {
            return edemaCoefficient;
        }

        public void setExtracellularFluid(String extracellularFluid) {
            this.extracellularFluid = extracellularFluid;
        }

        public String getExtracellularFluid() {
            return extracellularFluid;
        }

        public void setIntracellularFluid(String intracellularFluid) {
            this.intracellularFluid = intracellularFluid;
        }

        public String getIntracellularFluid() {
            return intracellularFluid;
        }

        public void setBmi(String bmi) {
            this.bmi = bmi;
        }

        public String getBmi() {
            return bmi;
        }
    }

    private static String insertThird(BodyComposition.Third t, String FORMAT) {
        return String.format(FORMAT, t.getCur());
    }

    public static final String FLOAT_2_FORMAT                      = "%.2f";
    public static final String FLOAT_1_FORMAT                      = "%.1f";
    public static final String FLOAT_0_FORMAT                      = "%.0f";
    public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static MasterFitEntity toHere(Record record) {
        MasterFitEntity sr = new MasterFitEntity();
        if (record == null) {
            return sr;
        }
        BodyComposition bc = record.getBodyComposition();
        RecordsBean recordBean = new RecordsBean();
        List<RecordsBean> mRecords = new ArrayList<RecordsBean>();
        recordBean.setUid(record.getName());
        recordBean.setGender(insertThird(bc.性别, FLOAT_0_FORMAT));
        recordBean.setAge(insertThird(bc.年龄, FLOAT_0_FORMAT));
        recordBean.setHeight(insertThird(bc.身高, FLOAT_0_FORMAT));
        recordBean.setWeight(insertThird(bc.体重, FLOAT_2_FORMAT));
        recordBean.setLeanBody(insertThird(bc.去脂体重, FLOAT_2_FORMAT));
        recordBean.setMuscle(insertThird(bc.肌肉量, FLOAT_2_FORMAT));
        recordBean.setBasalMetabolism(insertThird(bc.基础代谢, FLOAT_0_FORMAT));
        recordBean.setBodyAge(insertThird(bc.身体年龄, FLOAT_0_FORMAT));
        recordBean.setBodyMoisture(insertThird(bc.身体水分, FLOAT_2_FORMAT));
        recordBean.setPbf(insertThird(bc.体脂百分比, FLOAT_2_FORMAT));
        recordBean.setProtein(insertThird(bc.蛋白质, FLOAT_2_FORMAT));
        recordBean.setMineralSalt(insertThird(bc.无机盐, FLOAT_2_FORMAT));
        recordBean.setRaFat(insertThird(bc.右上脂肪量, FLOAT_2_FORMAT));
        recordBean.setLaMuscle(insertThird(bc.左上肢肌肉量, FLOAT_2_FORMAT));
        recordBean.setRaMuscle(insertThird(bc.右上肢肌肉量, FLOAT_2_FORMAT));
        recordBean.setRlFat(insertThird(bc.右下脂肪量, FLOAT_2_FORMAT));
        recordBean.setLaFat(insertThird(bc.左上脂肪量, FLOAT_2_FORMAT));
        recordBean.setLlFat(insertThird(bc.左下脂肪量, FLOAT_2_FORMAT));
        recordBean.setLlMuscle(insertThird(bc.左下肌肉量, FLOAT_2_FORMAT));
        recordBean.setRlMuscle(insertThird(bc.右下肌肉量, FLOAT_2_FORMAT));
        recordBean.setGrade(insertThird(bc.评分, FLOAT_2_FORMAT));
        recordBean.setSkeletalMuscle(insertThird(bc.骨骼肌, FLOAT_2_FORMAT));
        recordBean.setTotalEnergy(insertThird(bc.总能耗, FLOAT_2_FORMAT));
        recordBean.setTrunkFat(insertThird(bc.躯干脂肪量, FLOAT_2_FORMAT));
        recordBean.setTrunkMuscle(insertThird(bc.躯干肌肉量, FLOAT_2_FORMAT));
        recordBean.setVisceralArea(insertThird(bc.内脏面积, FLOAT_2_FORMAT));
        recordBean.setWhr(insertThird(bc.腰臀比, FLOAT_2_FORMAT));
        recordBean.setCompleteTime(formatter.format(record.getTime()));

//        recordBean.setBmi(insertThird(bc.BMI, FLOAT_1_FORMAT));
        recordBean.setBodyFat(insertThird(bc.体脂肪量, FLOAT_2_FORMAT));
//        recordBean.setBodyTypeAnalysis(insertThird(bc.体型分析, FLOAT_2_FORMAT));
//        recordBean.setEdemaCoefficient(insertThird(bc.水肿系数, FLOAT_2_FORMAT));
//        recordBean.setExtracellularFluid(insertThird(bc.细胞外液含量, FLOAT_2_FORMAT));
//        recordBean.setIntracellularFluid(insertThird(bc.细胞内液含量, FLOAT_2_FORMAT));
//        recordBean.setZ5k(insertThird(bc._5k电阻, FLOAT_2_FORMAT));
//        recordBean.setZ50k(insertThird(bc._50k电阻, FLOAT_2_FORMAT));
//        recordBean.setZ250k(insertThird(bc._250k电阻, FLOAT_2_FORMAT));

        //recordBean.setName(insertThird(, FLOAT_0_FORMAT)); // Not bodyCompsition

        mRecords.add(recordBean);
//        sr.setImg(record.getImg());
        sr.setRecords(mRecords);
        sr.setSn(App.getSn());
//        sr.setUid(record.getName());
        sr.setTimestamp(System.currentTimeMillis() + "");
        sr.setSign(md5(sr.timestamp + "dj^_HNma2#1aPf"));
//        sr.setCompleteTime(formatter.format(record.getTime()));
        return sr;
    }

    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
