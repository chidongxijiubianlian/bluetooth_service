package org.andon.bluetooth_service.mapper;


import org.andon.bluetooth_service.entity.TestUserDevice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDeviceMapper {
    int insert(TestUserDevice TestUserDevice);
    List<TestUserDevice> getByUserID(int uid);
    List<TestUserDevice> getByDeviceID(int did);
    TestUserDevice getByID(int uid, int did);
    void del(int id);
}
