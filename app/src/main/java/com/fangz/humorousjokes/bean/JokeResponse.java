package com.fangz.humorousjokes.bean;

import java.util.List;

/**
 * Created by zhangtao on 2017/9/21.
 */

public class JokeResponse {

    /**
     * error_code : 0
     * reason : Success
     * result : {"data":[{"content":"今儿在公司楼下饮料摊，服务员妹子问我：可乐加冰吗？ 我问：难道还能加别的？ 她：想加啥都行啊！ 我答：那咱俩加个微信吧！","hashId":"5ffd82fd7742a85461e24b603efac82c","unixtime":1504583030,"updatetime":"2017-09-05 11:43:50"}]}
     */

    private int error_code;
    private String reason;
    private ResultData result;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ResultData getResult() {
        return result;
    }

    public void setResult(ResultData result) {
        this.result = result;
    }

    public static class ResultData {
        private List<JokeContent> data;

        public List<JokeContent> getData() {
            return data;
        }

        public void setData(List<JokeContent> data) {
            this.data = data;
        }

        public static class JokeContent {
            /**
             * content : 今儿在公司楼下饮料摊，服务员妹子问我：可乐加冰吗？ 我问：难道还能加别的？ 她：想加啥都行啊！ 我答：那咱俩加个微信吧！
             * hashId : 5ffd82fd7742a85461e24b603efac82c
             * unixtime : 1504583030
             * updatetime : 2017-09-05 11:43:50
             */

            private String content;
            private String hashId;
            private int unixtime;
            private String updatetime;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getHashId() {
                return hashId;
            }

            public void setHashId(String hashId) {
                this.hashId = hashId;
            }

            public int getUnixtime() {
                return unixtime;
            }

            public void setUnixtime(int unixtime) {
                this.unixtime = unixtime;
            }

            public String getUpdatetime() {
                return updatetime;
            }

            public void setUpdatetime(String updatetime) {
                this.updatetime = updatetime;
            }

            @Override
            public String toString() {
                return "JokeContent{" +
                        "content='" + content + '\'' +
                        ", hashId='" + hashId + '\'' +
                        ", unixtime=" + unixtime +
                        ", updatetime='" + updatetime + '\'' +
                        '}';
            }
        }
    }

    @Override
    public String toString() {
        return "JokeResponse{" +
                "error_code=" + error_code +
                ", reason='" + reason + '\'' +
                ", result=" + result +
                '}';
    }
}
