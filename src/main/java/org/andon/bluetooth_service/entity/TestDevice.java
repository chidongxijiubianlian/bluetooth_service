package org.andon.bluetooth_service.entity;

import java.util.Date;

public class TestDevice {
    private int id;
    private String deviceID;
    private String name;
    private String bluetoothkey;
    private int ownerid;
    private Date createdate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBluetoothkey() {
        return bluetoothkey;
    }

    public void setBluetoothkey(String bluetoothkey) {
        this.bluetoothkey = bluetoothkey;
    }

    public int getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(int ownerid) {
        this.ownerid = ownerid;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }
}
