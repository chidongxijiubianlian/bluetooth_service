package org.andon.bluetooth_service.mapper;

import org.andon.bluetooth_service.entity.TestFingerprint;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FingerprintMapper {
    int insert(TestFingerprint TestFingerprint);
    void del(String guid);
    int any(String guid);
    List<TestFingerprint> get(String deviceID, String iphone);
    int update(TestFingerprint TestFingerprint);
    int anyFingerprint(TestFingerprint TestFingerprint);

}
