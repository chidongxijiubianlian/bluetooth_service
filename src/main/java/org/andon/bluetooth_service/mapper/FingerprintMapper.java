package org.andon.bluetooth_service.mapper;

import org.andon.bluetooth_service.entity.TestFingerprint;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FingerprintMapper {
    int insert(TestFingerprint TestFingerprint);
    void del(String guid,String phone);
    int any(String guid,String phone);
    List<TestFingerprint> get(String deviceID, String phone);
    int update(TestFingerprint TestFingerprint);
    int anyFingerprint(TestFingerprint TestFingerprint);

}
