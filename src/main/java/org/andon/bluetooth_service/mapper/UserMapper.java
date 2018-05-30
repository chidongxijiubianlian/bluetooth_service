package org.andon.bluetooth_service.mapper;

import org.andon.bluetooth_service.entity.TestUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    int insert(TestUser TestUser);
    TestUser getByPhone(String phone);
    List<TestUser> getByIDList(List<Integer> idList);
    void del(String phone);
}
