package org.andon.bluetooth_service.dto;

public class DTO_AddFingerprint {

    private String phone;
    private String deviceID;
    private String fingerprint;
    private int fingerprintID;
    private String fingerprintUID;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public int getFingerprintID() {
        return fingerprintID;
    }

    public void setFingerprintID(int fingerprintID) {
        this.fingerprintID = fingerprintID;
    }

    public String getFingerprintUID() {
        return fingerprintUID;
    }

    public void setFingerprintUID(String fingerprintUID) {
        this.fingerprintUID = fingerprintUID;
    }
}
