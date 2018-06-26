package org.andon.bluetooth_service.dto;

public class DTO_GetUnlockrecord {

    private String deviceID;
    private String phone;

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
