package org.andon.bluetooth_service.dto;

public class DTO_Fingerprint {

    private String guid;
    private String content;
    private String createdate;
    private int fingerprintID;
    private String fingerprintUID;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
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
