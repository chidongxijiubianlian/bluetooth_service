package org.andon.bluetooth_service.service.impl;

import org.andon.bluetooth_service.entity.TestDevice;
import org.andon.bluetooth_service.entity.TestUser;
import org.andon.bluetooth_service.entity.TestUserDevice;
import org.andon.bluetooth_service.mapper.DeviceMapper;
import org.andon.bluetooth_service.mapper.UserDeviceMapper;
import org.andon.bluetooth_service.mapper.UserMapper;
import org.andon.bluetooth_service.ret.RETSharedDeviceToUsers;
import org.andon.bluetooth_service.ret.RETSharedUser;
import org.andon.bluetooth_service.service.IDeviceUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Component
public class DeviceUsers implements IDeviceUsers {
    @Autowired
    private DeviceMapper _deviceMapper;
    @Autowired
    private UserDeviceMapper _userdeviceMapper;
    @Autowired
    private UserMapper _userMapper;
    public RETSharedDeviceToUsers GetDeviceUsers(String deviceID) {
        RETSharedDeviceToUsers retSharedDeviceToUsers = new RETSharedDeviceToUsers();
        List<RETSharedUser> sharedUser = new ArrayList<>();
        retSharedDeviceToUsers.setDeviceID(deviceID);
        TestDevice testDevice =_deviceMapper.getByDeviceID(deviceID);
        if(testDevice ==null)
        {
            return null;
        }
        List<TestUserDevice> testUserDeviceList =_userdeviceMapper.getByDeviceID(testDevice.getId());
        if(testUserDeviceList !=null && testUserDeviceList.size() >0)
        {
            List<Integer> userIDList = testUserDeviceList.stream().map(TestUserDevice::getUid).collect(Collectors.toList());
            List<TestUser> testUserList = _userMapper.getByIDList(userIDList);
            if(testUserList !=null && testUserList.size()>0)
            {
                for(TestUser testUser:testUserList)
                {
                    RETSharedUser retSharedUser = new RETSharedUser();
                    retSharedUser.setPhone(testUser.getPhone());
                    sharedUser.add(retSharedUser);
                }
            }
            retSharedDeviceToUsers.setSharedUser(sharedUser);
        }
        return retSharedDeviceToUsers;
    }
}
