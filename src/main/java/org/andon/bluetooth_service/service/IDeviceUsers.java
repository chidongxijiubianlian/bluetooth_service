package org.andon.bluetooth_service.service;

import org.andon.bluetooth_service.ret.RETSharedDeviceToUsers;

public interface IDeviceUsers {
    RETSharedDeviceToUsers GetDeviceUsers(String deviceID);
}
