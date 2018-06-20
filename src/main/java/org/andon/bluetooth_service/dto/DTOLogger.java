package org.andon.bluetooth_service.dto;

import com.alibaba.fastjson.annotation.JSONField;

public class DTOLogger {

    @JSONField(ordinal = 1)
    private String APIName;
    @JSONField(ordinal = 2)
    private String RequestUrl;
    @JSONField(ordinal = 3)
    private String PostData;
    @JSONField(ordinal = 4)
    private String ResponseData;
    @JSONField(ordinal = 5)
    private String RequestTime;
    @JSONField(ordinal = 6)
    private String ResponseTime;

    public String getAPIName() {
        return APIName;
    }

    public void setAPIName(String APIName) {
        this.APIName = APIName;
    }

    public String getRequestUrl() {
        return RequestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        RequestUrl = requestUrl;
    }

    public String getRequestTime() {
        return RequestTime;
    }

    public void setRequestTime(String requestTime) {
        RequestTime = requestTime;
    }

    public String getResponseTime() {
        return ResponseTime;
    }

    public void setResponseTime(String responseTime) {
        ResponseTime = responseTime;
    }

    public String getPostData() {
        return PostData;
    }

    public void setPostData(String postData) {
        PostData = postData;
    }

    public String getResponseData() {
        return ResponseData;
    }

    public void setResponseData(String responseData) {
        ResponseData = responseData;
    }
}
