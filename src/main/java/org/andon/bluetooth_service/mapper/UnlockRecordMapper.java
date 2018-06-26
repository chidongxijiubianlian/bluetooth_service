package org.andon.bluetooth_service.mapper;

import org.andon.bluetooth_service.entity.TestFingerprint;
import org.andon.bluetooth_service.entity.TestUnlockrecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UnlockRecordMapper {
    int insertBatch(List<TestUnlockrecord> list);
    List<TestUnlockrecord> select(String deviceID, String phone,String date);
    TestFingerprint selectFingerprint(String fingerprintUID);

}
