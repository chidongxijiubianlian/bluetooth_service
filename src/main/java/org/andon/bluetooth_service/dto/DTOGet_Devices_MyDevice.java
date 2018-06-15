package org.andon.bluetooth_service.dto;

import java.util.List;

public class DTOGet_Devices_MyDevice {
    private String deviceID;
    private String deviceName;
    private List<DTO_Fingerprint> fingerprint;

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public List<DTO_Fingerprint> getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(List<DTO_Fingerprint> fingerprint) {
        this.fingerprint = fingerprint;
    }
}
