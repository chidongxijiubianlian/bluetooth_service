package org.andon.bluetooth_service.dto;

import java.util.List;

public class DTO_AddUnlockrecord {

    private String phone;
    private String deviceID;
    private List<DTO_Unlockrecord> unlockRecords;

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

    public List<DTO_Unlockrecord> getUnlockRecords() {
        return unlockRecords;
    }

    public void setUnlockRecords(List<DTO_Unlockrecord> unlockRecords) {
        this.unlockRecords = unlockRecords;
    }
}
