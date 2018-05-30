package org.andon.bluetooth_service.ret;

import java.util.List;

public class RETSharedDeviceToUsers {
    private String DeviceID;
    private List<RETSharedUser> SharedUser;

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public List<RETSharedUser> getSharedUser() {
        return SharedUser;
    }

    public void setSharedUser(List<RETSharedUser> RETSharedUser) {
        this.SharedUser = RETSharedUser;
    }

}
