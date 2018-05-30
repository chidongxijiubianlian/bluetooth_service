package org.andon.bluetooth_service.dto;

import java.util.List;

public class DTOGet_Devices {
    private List<DTOGet_Devices_MyDevice> devices;
    private List<DTOGet_Devices_SharedDevice> sharedDevices;

    public List<DTOGet_Devices_MyDevice> getDevices() {
        return devices;
    }

    public void setDevices(List<DTOGet_Devices_MyDevice> devices) {
        this.devices = devices;
    }

    public List<DTOGet_Devices_SharedDevice> getSharedDevices() {
        return sharedDevices;
    }

    public void setSharedDevices(List<DTOGet_Devices_SharedDevice> sharedDevices) {
        this.sharedDevices = sharedDevices;
    }
}
