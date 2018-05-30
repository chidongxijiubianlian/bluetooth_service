package org.andon.bluetooth_service.dto;

public class DTOUnShareBluetooth {
    private String Phone;
    private String UnSharedPhone;
    private String DeviceID;

    public String getUnSharedPhone() {
        return UnSharedPhone;
    }

    public void setUnSharedPhone(String unSharedPhone) {
        UnSharedPhone = unSharedPhone;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }



    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }
}
