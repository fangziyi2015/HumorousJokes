package com.fangz.humorousjokes.bean;

import java.util.List;

/**
 * Created by zhangtao on 2017/9/22.
 */

public class CitysResponse {


    /**
     * resultcode : 200
     * reason : successed
     * result : [{"id":"27","province":"上海","city":"上海","district":"浦东"}]
     * error_code : 0
     */

    private String resultcode;
    private String reason;
    private int error_code;
    private List<ResultCity> result;

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public List<ResultCity> getResult() {
        return result;
    }

    public void setResult(List<ResultCity> result) {
        this.result = result;
    }

    public static class ResultCity {
        /**
         * id : 27
         * province : 上海
         * city : 上海
         * district : 浦东
         */

        private String id;
        private String province;
        private String city;
        private String district;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }
    }
}
