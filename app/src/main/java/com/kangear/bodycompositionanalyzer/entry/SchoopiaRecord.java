package com.kangear.bodycompositionanalyzer.entry;

import android.util.Log;

import com.google.gson.Gson;
import com.kangear.bodycompositionanalyzer.BodyComposition;
import com.kangear.bodycompositionanalyzer.Record;
import com.kangear.bodycompositionanalyzer.application.App;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 第一次使用这种json
 * 把BodyCompostion和Record转换成SchoopiaRecord
 */
public class SchoopiaRecord {
    private static final String TAG = "SchoopiaRecord";
    /**
     * completeTime : 2019-07-02  11:30:24
     * sn : record
     * uid : xxx
     * record : {"name":["123","",""],"gender":["女","",""],"age":["19","",""],"height":["127","",""],"weight":["45.3","29.8","38.7"],"z5k":["32.4","",""],"z50k":["32.4","",""],"z250k":["32.4","",""],"leanBody":["32.4","24.7","28.3"],"bodyFat":["12.9","7.0","10.6"],"muscle":["30.0","22.8","26.1"],"skeletalMuscle":["14.0","",""],"bodyMoisture":["22.1","17.8","20.3"],"protein":["7.9","4.9","5.6"],"mineralSalt":["2.4","1.9","2.0"],"extracellularFluid":["6.2","7.7","8.8"],"intracellularFluid":["15.9","13.2","14.3"],"laMuscle":["1.80","",""],"raMuscle":["3.10","",""],"trunkMuscle":["15.50","",""],"llMuscle":["3.00","",""],"rlMuscle":["6.60","",""],"laFat":["0.80","",""],"raFat":["0.50","",""],"trunkFat":["7.10","",""],"llFat":["3.10","",""],"rlFat":["1.40","",""],"BMI":["28.0","",""],"pbf":["28.4","",""],"whr":["0.87","0.70","0.80"],"edemaCoefficient":["0.28","0.30","0.35"],"bodyTypeAnalysis":["0","1","3"],"visceralArea":["84.6","0.0","75.0"],"grade":["79.7","",""],"basalMetabolism":["1137","",""],"totalEnergy":["1695","",""],"bodyAge":["20","",""]}
     */
    private String completeTime;
    private String sn;
    private String uid;
    private RecordBean record;
    public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String getCompleteTime() {
        return completeTime;
    }

    public Date getCompleteTimeAsLong() {
        Date l = new Date();
        try {
            l = SchoopiaRecord.formatter.parse(completeTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return l;
    }

    public void setCompleteTime(String completeTime) {
        this.completeTime = completeTime;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public RecordBean getRecord() {
        return record;
    }

    public void setRecord(RecordBean record) {
        this.record = record;
    }

    public static class RecordBean {
        private List<String> name;
        private List<String> gender;
        private List<String> age;
        private List<String> height;
        private List<String> weight;
        private List<String> z5k;
        private List<String> z50k;
        private List<String> z250k;
        private List<String> leanBody;
        private List<String> bodyFat;
        private List<String> muscle;
        private List<String> skeletalMuscle;
        private List<String> bodyMoisture;
        private List<String> protein;
        private List<String> mineralSalt;
        private List<String> extracellularFluid;
        private List<String> intracellularFluid;
        private List<String> laMuscle;
        private List<String> raMuscle;
        private List<String> trunkMuscle;
        private List<String> llMuscle;
        private List<String> rlMuscle;
        private List<String> laFat;
        private List<String> raFat;
        private List<String> trunkFat;
        private List<String> llFat;
        private List<String> rlFat;
        private List<String> bmi;
        private List<String> pbf;
        private List<String> whr;
        private List<String> edemaCoefficient;
        private List<String> bodyTypeAnalysis;
        private List<String> visceralArea;
        private List<String> grade;
        private List<String> basalMetabolism;
        private List<String> totalEnergy;
        private List<String> bodyAge;

        public List<String> getName() {
            return name;
        }

        public void setName(List<String> name) {
            this.name = name;
        }

        public List<String> getGender() {
            return gender;
        }

        public void setGender(List<String> gender) {
            this.gender = gender;
        }

        public List<String> getAge() {
            return age;
        }

        public void setAge(List<String> age) {
            this.age = age;
        }

        public List<String> getHeight() {
            return height;
        }

        public void setHeight(List<String> height) {
            this.height = height;
        }

        public List<String> getWeight() {
            return weight;
        }

        public void setWeight(List<String> weight) {
            this.weight = weight;
        }

        public List<String> getZ5k() {
            return z5k;
        }

        public void setZ5k(List<String> z5k) {
            this.z5k = z5k;
        }

        public List<String> getZ50k() {
            return z50k;
        }

        public void setZ50k(List<String> z50k) {
            this.z50k = z50k;
        }

        public List<String> getZ250k() {
            return z250k;
        }

        public void setZ250k(List<String> z250k) {
            this.z250k = z250k;
        }

        public List<String> getLeanBody() {
            return leanBody;
        }

        public void setLeanBody(List<String> leanBody) {
            this.leanBody = leanBody;
        }

        public List<String> getBodyFat() {
            return bodyFat;
        }

        public void setBodyFat(List<String> bodyFat) {
            this.bodyFat = bodyFat;
        }

        public List<String> getMuscle() {
            return muscle;
        }

        public void setMuscle(List<String> muscle) {
            this.muscle = muscle;
        }

        public List<String> getSkeletalMuscle() {
            return skeletalMuscle;
        }

        public void setSkeletalMuscle(List<String> skeletalMuscle) {
            this.skeletalMuscle = skeletalMuscle;
        }

        public List<String> getBodyMoisture() {
            return bodyMoisture;
        }

        public void setBodyMoisture(List<String> bodyMoisture) {
            this.bodyMoisture = bodyMoisture;
        }

        public List<String> getProtein() {
            return protein;
        }

        public void setProtein(List<String> protein) {
            this.protein = protein;
        }

        public List<String> getMineralSalt() {
            return mineralSalt;
        }

        public void setMineralSalt(List<String> mineralSalt) {
            this.mineralSalt = mineralSalt;
        }

        public List<String> getExtracellularFluid() {
            return extracellularFluid;
        }

        public void setExtracellularFluid(List<String> extracellularFluid) {
            this.extracellularFluid = extracellularFluid;
        }

        public List<String> getIntracellularFluid() {
            return intracellularFluid;
        }

        public void setIntracellularFluid(List<String> intracellularFluid) {
            this.intracellularFluid = intracellularFluid;
        }

        public List<String> getLaMuscle() {
            return laMuscle;
        }

        public void setLaMuscle(List<String> laMuscle) {
            this.laMuscle = laMuscle;
        }

        public List<String> getRaMuscle() {
            return raMuscle;
        }

        public void setRaMuscle(List<String> raMuscle) {
            this.raMuscle = raMuscle;
        }

        public List<String> getTrunkMuscle() {
            return trunkMuscle;
        }

        public void setTrunkMuscle(List<String> trunkMuscle) {
            this.trunkMuscle = trunkMuscle;
        }

        public List<String> getLlMuscle() {
            return llMuscle;
        }

        public void setLlMuscle(List<String> llMuscle) {
            this.llMuscle = llMuscle;
        }

        public List<String> getRlMuscle() {
            return rlMuscle;
        }

        public void setRlMuscle(List<String> rlMuscle) {
            this.rlMuscle = rlMuscle;
        }

        public List<String> getLaFat() {
            return laFat;
        }

        public void setLaFat(List<String> laFat) {
            this.laFat = laFat;
        }

        public List<String> getRaFat() {
            return raFat;
        }

        public void setRaFat(List<String> raFat) {
            this.raFat = raFat;
        }

        public List<String> getTrunkFat() {
            return trunkFat;
        }

        public void setTrunkFat(List<String> trunkFat) {
            this.trunkFat = trunkFat;
        }

        public List<String> getLlFat() {
            return llFat;
        }

        public void setLlFat(List<String> llFat) {
            this.llFat = llFat;
        }

        public List<String> getRlFat() {
            return rlFat;
        }

        public void setRlFat(List<String> rlFat) {
            this.rlFat = rlFat;
        }

        public List<String> getBmi() {
            return bmi;
        }

        public void setBmi(List<String> bmi) {
            this.bmi = bmi;
        }

        public List<String> getPbf() {
            return pbf;
        }

        public void setPbf(List<String> pbf) {
            this.pbf = pbf;
        }

        public List<String> getWhr() {
            return whr;
        }

        public void setWhr(List<String> whr) {
            this.whr = whr;
        }

        public List<String> getEdemaCoefficient() {
            return edemaCoefficient;
        }

        public void setEdemaCoefficient(List<String> edemaCoefficient) {
            this.edemaCoefficient = edemaCoefficient;
        }

        public List<String> getBodyTypeAnalysis() {
            return bodyTypeAnalysis;
        }

        public void setBodyTypeAnalysis(List<String> bodyTypeAnalysis) {
            this.bodyTypeAnalysis = bodyTypeAnalysis;
        }

        public List<String> getVisceralArea() {
            return visceralArea;
        }

        public void setVisceralArea(List<String> visceralArea) {
            this.visceralArea = visceralArea;
        }

        public List<String> getGrade() {
            return grade;
        }

        public void setGrade(List<String> grade) {
            this.grade = grade;
        }

        public List<String> getBasalMetabolism() {
            return basalMetabolism;
        }

        public void setBasalMetabolism(List<String> basalMetabolism) {
            this.basalMetabolism = basalMetabolism;
        }

        public List<String> getTotalEnergy() {
            return totalEnergy;
        }

        public void setTotalEnergy(List<String> totalEnergy) {
            this.totalEnergy = totalEnergy;
        }

        public List<String> getBodyAge() {
            return bodyAge;
        }

        public void setBodyAge(List<String> bodyAge) {
            this.bodyAge = bodyAge;
        }

        @Override
        public String toString() {
            return "RecordBean{" +
                    "name=" + name +
                    ", gender=" + gender +
                    ", age=" + age +
                    ", height=" + height +
                    ", weight=" + weight +
                    ", z5k=" + z5k +
                    ", z50k=" + z50k +
                    ", z250k=" + z250k +
                    ", leanBody=" + leanBody +
                    ", bodyFat=" + bodyFat +
                    ", muscle=" + muscle +
                    ", skeletalMuscle=" + skeletalMuscle +
                    ", bodyMoisture=" + bodyMoisture +
                    ", protein=" + protein +
                    ", mineralSalt=" + mineralSalt +
                    ", extracellularFluid=" + extracellularFluid +
                    ", intracellularFluid=" + intracellularFluid +
                    ", laMuscle=" + laMuscle +
                    ", raMuscle=" + raMuscle +
                    ", trunkMuscle=" + trunkMuscle +
                    ", llMuscle=" + llMuscle +
                    ", rlMuscle=" + rlMuscle +
                    ", laFat=" + laFat +
                    ", raFat=" + raFat +
                    ", trunkFat=" + trunkFat +
                    ", llFat=" + llFat +
                    ", rlFat=" + rlFat +
                    ", bmi=" + bmi +
                    ", pbf=" + pbf +
                    ", whr=" + whr +
                    ", edemaCoefficient=" + edemaCoefficient +
                    ", bodyTypeAnalysis=" + bodyTypeAnalysis +
                    ", visceralArea=" + visceralArea +
                    ", grade=" + grade +
                    ", basalMetabolism=" + basalMetabolism +
                    ", totalEnergy=" + totalEnergy +
                    ", bodyAge=" + bodyAge +
                    '}';
        }
    }

    public static SchoopiaRecord toHere(Record record) {
        SchoopiaRecord sr = new SchoopiaRecord();
        if (record == null) {
            return sr;
        }
        BodyComposition bc = record.getBodyComposition();
        SchoopiaRecord.RecordBean recordBean = new SchoopiaRecord.RecordBean();

        recordBean.setAge(insertThird(bc.年龄, FLOAT_0_FORMAT));
        recordBean.setBasalMetabolism(insertThird(bc.基础代谢, FLOAT_0_FORMAT));
        recordBean.setBmi(insertThird(bc.BMI, FLOAT_1_FORMAT));
        recordBean.setBodyAge(insertThird(bc.身体年龄, FLOAT_0_FORMAT));
        recordBean.setBodyFat(insertThird(bc.体脂肪量, FLOAT_2_FORMAT));
        recordBean.setBodyMoisture(insertThird(bc.身体水分, FLOAT_2_FORMAT));
        recordBean.setBodyTypeAnalysis(insertThird(bc.体型分析, FLOAT_2_FORMAT));
        recordBean.setEdemaCoefficient(insertThird(bc.水肿系数, FLOAT_2_FORMAT));
        recordBean.setExtracellularFluid(insertThird(bc.细胞外液含量, FLOAT_2_FORMAT));
        recordBean.setGender(insertThird(bc.性别, FLOAT_0_FORMAT));
        recordBean.setGrade(insertThird(bc.评分, FLOAT_2_FORMAT));
        recordBean.setHeight(insertThird(bc.身高, FLOAT_0_FORMAT));
        recordBean.setIntracellularFluid(insertThird(bc.细胞内液含量, FLOAT_2_FORMAT));
        recordBean.setLaFat(insertThird(bc.左上脂肪量, FLOAT_2_FORMAT));
        recordBean.setLaMuscle(insertThird(bc.左上肢肌肉量, FLOAT_2_FORMAT));
        recordBean.setLeanBody(insertThird(bc.去脂体重, FLOAT_2_FORMAT));
        recordBean.setLlFat(insertThird(bc.左下脂肪量, FLOAT_2_FORMAT));
        recordBean.setLlMuscle(insertThird(bc.左下肌肉量, FLOAT_2_FORMAT));
        recordBean.setMineralSalt(insertThird(bc.无机盐, FLOAT_2_FORMAT));
        recordBean.setMuscle(insertThird(bc.肌肉量, FLOAT_0_FORMAT));
//        recordBean.setName(insertThird(, FLOAT_0_FORMAT)); // Not bodyCompsition
        recordBean.setPbf(insertThird(bc.体脂百分比, FLOAT_2_FORMAT));
        recordBean.setProtein(insertThird(bc.蛋白质, FLOAT_2_FORMAT));
        recordBean.setRaFat(insertThird(bc.右上脂肪量, FLOAT_2_FORMAT));
        recordBean.setRaMuscle(insertThird(bc.右上肢肌肉量, FLOAT_2_FORMAT));
        recordBean.setRlFat(insertThird(bc.右下脂肪量, FLOAT_2_FORMAT));
        recordBean.setRlMuscle(insertThird(bc.右下肌肉量, FLOAT_2_FORMAT));
        recordBean.setSkeletalMuscle(insertThird(bc.骨骼肌, FLOAT_2_FORMAT));
        recordBean.setTotalEnergy(insertThird(bc.总能耗, FLOAT_2_FORMAT));
        recordBean.setTrunkFat(insertThird(bc.躯干脂肪量, FLOAT_2_FORMAT));
        recordBean.setTrunkMuscle(insertThird(bc.躯干肌肉量, FLOAT_2_FORMAT));
        recordBean.setVisceralArea(insertThird(bc.内脏面积, FLOAT_2_FORMAT));
        recordBean.setWeight(insertThird(bc.体重, FLOAT_2_FORMAT));
        recordBean.setWhr(insertThird(bc.腰臀比, FLOAT_2_FORMAT));
        recordBean.setZ5k(insertThird(bc._5k电阻, FLOAT_2_FORMAT));
        recordBean.setZ50k(insertThird(bc._50k电阻, FLOAT_2_FORMAT));
        recordBean.setZ250k(insertThird(bc._250k电阻, FLOAT_2_FORMAT));

        sr.setRecord(recordBean);
        sr.setSn(App.getSn());
        sr.setUid(record.getName());
        sr.setCompleteTime(formatter.format(record.getTime()));
        return sr;
    }

    public static final String FLOAT_2_FORMAT                      = "%.2f";
    public static final String FLOAT_1_FORMAT                      = "%.1f";
    public static final String FLOAT_0_FORMAT                      = "%.0f";

    private static List<String> insertThird(BodyComposition.Third t, String FORMAT) {
        List<String> tmpList = new ArrayList<String>();
        tmpList.add(String.format(FORMAT, t.getCur()));
        tmpList.add(String.format(FORMAT, t.getMin()));
        tmpList.add(String.format(FORMAT, t.getMax()));
        return tmpList;
    }

    @Override
    public String toString() {
        return "SchoopiaRecord{" +
                "completeTime='" + completeTime + '\'' +
                ", sn='" + sn + '\'' +
                ", uid='" + uid + '\'' +
                ", record=" + record +
                '}';
    }

    public static void sendPost(final String json) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    post(json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    public static void post(String json) throws Exception {
        // 同时传递到两个位置
        Log.i(TAG, "REPORT: " + json);
        post2(json, "http://b.schoopia.com/bpc/sport-dt/dt/physical/remote");
        post2(json, "https://www.kangear.com/lala/physical");
    }

    public static void post2(String json, String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("token", "f1258c93e974");
        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        conn.setRequestProperty("Accept","application/json;charset=UTF-8");
        conn.setDoOutput(true);
        conn.setDoInput(true);

        DataOutputStream os = new DataOutputStream(conn.getOutputStream());
        //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
        os.write(json.getBytes(Charset.forName("UTF-8")));

        os.flush();
        os.close();

        Log.i("STATUS", String.valueOf(conn.getResponseCode()));
        Log.i("MSG" , conn.getResponseMessage());

        BufferedReader reader = null;
        InputStream is = conn.getInputStream();
        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        String strRead = null;
        StringBuffer sbf = new StringBuffer();
        while ((strRead = reader.readLine()) != null) {
            sbf.append(strRead);
            sbf.append("\r\n");
        }
        reader.close();
        String result = sbf.toString();
        Log.e(TAG, "RESPONSE: " + result);

        Gson gson = new Gson();
        SchoopiaAddResult ar = gson.fromJson(result, SchoopiaAddResult.class);
        Log.e(TAG, ar.toString());

        conn.disconnect();
    }
}
