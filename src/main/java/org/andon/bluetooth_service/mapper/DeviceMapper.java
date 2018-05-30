package org.andon.bluetooth_service.mapper;


import org.andon.bluetooth_service.entity.TestDevice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DeviceMapper {
    int insert(TestDevice TestDevice);
    void update(TestDevice TestDevice);
    TestDevice getByDeviceID(String deviceID);
    List<TestDevice> getByOwnerID(int uid);
    void del(String phone);
    List<TestDevice> getByDids(List<Integer> didList);
}
