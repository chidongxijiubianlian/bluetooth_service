package org.andon.bluetooth_service.dto;

public class DTOShareBluetooth {
    private String Phone;
    private String SharedPhone;
    private String DeviceID;

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getSharedPhone() {
        return SharedPhone;
    }

    public void setSharedPhone(String sharedPhone) {
        SharedPhone = sharedPhone;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }
}
