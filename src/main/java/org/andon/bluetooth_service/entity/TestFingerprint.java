package org.andon.bluetooth_service.entity;

import java.util.Date;

public class TestFingerprint {
    private int id;
    private String guid;
    private String phone;
    private String deviceID;
    private String fingerprint;
    private Date createdate;
    private int fingerprintID;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

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

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public int getFingerprintID() {
        return fingerprintID;
    }

    public void setFingerprintID(int fingerprintID) {
        this.fingerprintID = fingerprintID;
    }
}
