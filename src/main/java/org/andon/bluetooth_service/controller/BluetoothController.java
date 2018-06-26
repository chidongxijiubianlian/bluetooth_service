package org.andon.bluetooth_service.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.andon.bluetooth_service.base.BaseController;
import org.andon.bluetooth_service.base.ResponseEntity;
import org.andon.bluetooth_service.common.BluetoothUtils;
import org.andon.bluetooth_service.common.DateUtils;
import org.andon.bluetooth_service.dto.*;
import org.andon.bluetooth_service.entity.*;
import org.andon.bluetooth_service.mapper.*;
import org.andon.bluetooth_service.ret.RETPhoneInfo;
import org.andon.bluetooth_service.ret.RETSharedDeviceToUsers;
import org.andon.bluetooth_service.service.IDeviceUsers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class BluetoothController extends BaseController {
    @Autowired
    private DeviceMapper _deviceMapper;
    @Autowired
    private UserDeviceMapper _userdeviceMapper;
    @Autowired
    private UserMapper _userMapper;
    @Autowired
    private IDeviceUsers _ideviceUsers;
    @Autowired
    private FingerprintMapper _fingerprintMapper;
    @Autowired
    private UnlockRecordMapper _unlockrecordMapper;

    /**
     * 引入日志，注意都是"org.slf4j"包下
     */
    private final static Logger logger = LoggerFactory.getLogger(BluetoothController.class);

    @PostMapping("api/bind_bluetooth")
    public ResponseEntity<?> bind_bluetooth(@RequestBody DTO_BindBluetooth dto_bindBluetooth) {
        /*--------------log------------------*/
        DTOLogger logDto = new DTOLogger();
        logDto.setRequestTime(DateUtils.getDateTimeStr(new Date()));
        logDto.setAPIName("bind_bluetooth");
        logDto.setRequestUrl("api/bind_bluetooth");
        logDto.setPostData(JSONArray.toJSONString(dto_bindBluetooth));
        /*--------------log------------------*/
        if (StringUtils.isEmpty(dto_bindBluetooth.getPhone()) || StringUtils.isEmpty(dto_bindBluetooth.getDeviceID()) || StringUtils.isEmpty(dto_bindBluetooth.getDeviceName())) {
            /*--------------log------------------*/
            logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
            logger.info(JSONObject.toJSONString(logDto));
            /*--------------log------------------*/
            return failResult(500.4, "请求缺少必要参数");
        }
        //判断phone的长度是否合理
        if (dto_bindBluetooth.getPhone().length() != 11) {
            /*--------------log------------------*/
            logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
            logger.info(JSONObject.toJSONString(logDto));
            /*--------------log------------------*/
            return failResult(500.5, "手机号长度不合法");
        }
        //判断手机号是否存在
        TestUser _testUser = _userMapper.getByPhone(dto_bindBluetooth.getPhone());
        if (_testUser == null) {
            _testUser = GetUser(dto_bindBluetooth.getPhone());
            _userMapper.insert(_testUser);
        }
        TestDevice _testDevice = _deviceMapper.getByDeviceID(dto_bindBluetooth.getDeviceID());
        if (_testDevice == null) {
            _testDevice = GetDevice(dto_bindBluetooth.getDeviceID(), dto_bindBluetooth.getDeviceName());
            _deviceMapper.insert(_testDevice);
        }
        if (_testDevice.getOwnerid() != 0 && _testDevice.getOwnerid() != _testUser.getId()) {
            /*--------------log------------------*/
            logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
            logger.info(JSONObject.toJSONString(logDto));
            /*--------------log------------------*/
            return failResult(500.6, "设备已绑定");
        }
        if (!_testDevice.getName().equals(dto_bindBluetooth.getDeviceName())) {
            _testDevice.setName(dto_bindBluetooth.getDeviceName());
        }
        _testDevice.setOwnerid(_testUser.getId());
        _deviceMapper.update(_testDevice);
        //先查询 手机号是否存在
        /*--------------log------------------*/
        logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
        logger.info(JSONObject.toJSONString(logDto));
        /*--------------log------------------*/
        return successResult(null);
    }

    @PostMapping("api/share_bluetooth")
    public ResponseEntity<?> share_bluetooth(@RequestBody DTOShareBluetooth dtoShareBluetooth) {
        /*--------------log------------------*/
        DTOLogger logDto = new DTOLogger();
        logDto.setRequestTime(DateUtils.getDateTimeStr(new Date()));
        logDto.setAPIName("share_bluetooth");
        logDto.setRequestUrl("api/share_bluetooth");
        logDto.setPostData(JSONArray.toJSONString(dtoShareBluetooth));
        /*--------------log------------------*/
        if (StringUtils.isEmpty(dtoShareBluetooth.getPhone()) || StringUtils.isEmpty(dtoShareBluetooth.getDeviceID()) || StringUtils.isEmpty(dtoShareBluetooth.getSharedPhone())) {
            /*--------------log------------------*/
            logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
            logger.info(JSONObject.toJSONString(logDto));
            /*--------------log------------------*/
            return failResult(500.4, "请求缺少必要参数");
        }
        if (dtoShareBluetooth.getSharedPhone().length() != 11) {
            /*--------------log------------------*/
            logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
            logger.info(JSONObject.toJSONString(logDto));
            /*--------------log------------------*/
            return failResult(500.5, "手机号长度不合法");
        }
        TestUser _testUser = _userMapper.getByPhone(dtoShareBluetooth.getPhone());
        if (_testUser == null) {
            /*--------------log------------------*/
            logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
            logger.info(JSONObject.toJSONString(logDto));
            /*--------------log------------------*/
            return failResult(500.1, "手机号不存在");
        }
        TestDevice _testDevice = _deviceMapper.getByDeviceID(dtoShareBluetooth.getDeviceID());
        if (_testDevice == null) {
            /*--------------log------------------*/
            logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
            logger.info(JSONObject.toJSONString(logDto));
            /*--------------log------------------*/
            return failResult(500.2, "设备不存在");
        }
        if (_testDevice.getOwnerid() != _testUser.getId()) {
            /*--------------log------------------*/
            logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
            logger.info(JSONObject.toJSONString(logDto));
            /*--------------log------------------*/
            return failResult(500.3, "非该设备绑定者，不能执行 分享/取消 分享操作");
        }
        TestUser _sharedUser = _userMapper.getByPhone(dtoShareBluetooth.getSharedPhone());
        if (_sharedUser == null) {
            _sharedUser = GetUser(dtoShareBluetooth.getSharedPhone());
            _userMapper.insert(_sharedUser);
        }
        TestUserDevice testUserDevice = _userdeviceMapper.getByID(_sharedUser.getId(), _testDevice.getId());
        if (testUserDevice == null) {
            testUserDevice = GetUserDevice(_sharedUser.getId(), _testDevice.getId());
            _userdeviceMapper.insert(testUserDevice);
        }
        RETSharedDeviceToUsers retSharedDeviceToUsers = new RETSharedDeviceToUsers();
        retSharedDeviceToUsers = _ideviceUsers.GetDeviceUsers(dtoShareBluetooth.getDeviceID());
        /*--------------log------------------*/
        logDto.setResponseData(JSONArray.toJSONString(retSharedDeviceToUsers));
        logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
        logger.info(JSONObject.toJSONString(logDto));
        /*--------------log------------------*/
        return successResult(retSharedDeviceToUsers);
    }

    @PostMapping("api/unshare_bluetooth")
    public ResponseEntity<?> unshare_bluetooth(@RequestBody DTOUnShareBluetooth dtoUnShareBluetooth) {
        /*--------------log------------------*/
        DTOLogger logDto = new DTOLogger();
        logDto.setRequestTime(DateUtils.getDateTimeStr(new Date()));
        logDto.setAPIName("unshare_bluetooth");
        logDto.setRequestUrl("api/unshare_bluetooth");
        logDto.setPostData(JSONArray.toJSONString(dtoUnShareBluetooth));
        /*--------------log------------------*/
        if (StringUtils.isEmpty(dtoUnShareBluetooth.getPhone()) || StringUtils.isEmpty(dtoUnShareBluetooth.getDeviceID()) || StringUtils.isEmpty(dtoUnShareBluetooth.getUnSharedPhone())) {
            /*--------------log------------------*/
            logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
            logger.info(JSONObject.toJSONString(logDto));
        /*--------------log------------------*/
            return failResult(500.4, "请求缺少必要参数");
        }
        TestUser _testUser = _userMapper.getByPhone(dtoUnShareBluetooth.getPhone());
        if (_testUser == null) {
            /*--------------log------------------*/
            logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
            logger.info(JSONObject.toJSONString(logDto));
        /*--------------log------------------*/
            return failResult(500.1, "手机号不存在");
        }
        TestDevice _testDevice = _deviceMapper.getByDeviceID(dtoUnShareBluetooth.getDeviceID());
        if (_testDevice == null) {
            /*--------------log------------------*/
            logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
            logger.info(JSONObject.toJSONString(logDto));
        /*--------------log------------------*/
            return failResult(500.2, "设备不存在");
        }
        if (_testDevice.getOwnerid() != _testUser.getId()) {
            /*--------------log------------------*/
            logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
            logger.info(JSONObject.toJSONString(logDto));
        /*--------------log------------------*/
            return failResult(500.3, "非该设备绑定者，不能执行 分享/取消 分享操作");
        }
        TestUser _sharedUser = _userMapper.getByPhone(dtoUnShareBluetooth.getUnSharedPhone());
        if (_sharedUser == null) {
            _sharedUser = GetUser(dtoUnShareBluetooth.getUnSharedPhone());
            _userMapper.insert(_sharedUser);
        }
        TestUserDevice testUserDevice = _userdeviceMapper.getByID(_sharedUser.getId(), _testDevice.getId());
        if (testUserDevice != null) {
            _userdeviceMapper.del(testUserDevice.getId());
        }
        RETSharedDeviceToUsers sharedUser = new RETSharedDeviceToUsers();
        sharedUser = _ideviceUsers.GetDeviceUsers(dtoUnShareBluetooth.getDeviceID());
        /*--------------log------------------*/
        logDto.setResponseData(JSONArray.toJSONString(sharedUser));
        logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
        logger.info(JSONObject.toJSONString(logDto));
        /*--------------log------------------*/
        return successResult(sharedUser);
    }

    @PostMapping("api/get_devices")
    public ResponseEntity<?> get_devices(@RequestBody DTOMy_Devices dtoMy_devices) {
        /*--------------log------------------*/
        DTOLogger logDto = new DTOLogger();
        logDto.setRequestTime(DateUtils.getDateTimeStr(new Date()));
        logDto.setAPIName("get_devices");
        logDto.setRequestUrl("api/get_devices");
        logDto.setPostData(JSONArray.toJSONString(dtoMy_devices));
        /*--------------log------------------*/
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        if (StringUtils.isEmpty(dtoMy_devices.getPhone())) {
            /*--------------log------------------*/
            logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
            logger.info(JSONObject.toJSONString(logDto));
        /*--------------log------------------*/
            return failResult(500.4, "请求缺少必要参数");
        }
        if (dtoMy_devices.getPhone().length() != 11) {
            /*--------------log------------------*/
            logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
            logger.info(JSONObject.toJSONString(logDto));
        /*--------------log------------------*/
            return failResult(500.5, "手机号长度不合法");
        }
        TestUser _testUser = _userMapper.getByPhone(dtoMy_devices.getPhone());
        if (_testUser == null) {
            _testUser = GetUser(dtoMy_devices.getPhone());
            _userMapper.insert(_testUser);
        }
        List<TestDevice> testDevicesList = _deviceMapper.getByOwnerID(_testUser.getId());
        List<TestUserDevice> testUserDeviceList = _userdeviceMapper.getByUserID(_testUser.getId());
        DTOGet_Devices dtoGet_devices = new DTOGet_Devices();
        dtoGet_devices.setDevices(new ArrayList<DTOGet_Devices_MyDevice>());
        dtoGet_devices.setSharedDevices(new ArrayList<DTOGet_Devices_SharedDevice>());
        if (testDevicesList != null && testDevicesList.size() > 0) {
            List<DTOGet_Devices_MyDevice> dtoGet_devices_myDevices = new ArrayList<>();
            for (TestDevice device : testDevicesList) {
                DTO_Fingerprint fDto = null;
                List<DTO_Fingerprint> tempList = new ArrayList<DTO_Fingerprint>();
                List<TestFingerprint> fList = _fingerprintMapper.get(device.getDeviceID(), dtoMy_devices.getPhone());
                for (TestFingerprint item : fList) {
                    fDto = new DTO_Fingerprint();
                    fDto.setGuid(item.getGuid());
                    fDto.setContent(item.getFingerprint());
                    fDto.setCreatedate(sdf.format(item.getCreatedate()));
                    fDto.setFingerprintID(item.getFingerprintID());
                    fDto.setFingerprintUID(item.getFingerprintUID());
                    tempList.add(fDto);
                }

                DTOGet_Devices_MyDevice myDevice = new DTOGet_Devices_MyDevice();
                myDevice.setDeviceID(device.getDeviceID());
                myDevice.setDeviceName(device.getName());
                myDevice.setFingerprint(tempList);

                dtoGet_devices_myDevices.add(myDevice);
            }
            dtoGet_devices.setDevices(dtoGet_devices_myDevices);
        }
        if (testUserDeviceList != null && testUserDeviceList.size() > 0) {
            List<DTOGet_Devices_SharedDevice> dtoGet_devices_myDevices = new ArrayList<>();
            List<Integer> didList = testUserDeviceList.stream().map(TestUserDevice::getDid).collect(Collectors.toList());
            List<TestDevice> testDeviceList = _deviceMapper.getByDids(didList);
            if (testDeviceList != null && testDeviceList.size() > 0) {
                List<DTOGet_Devices_SharedDevice> dtoGet_devices_sharedDevices = new ArrayList<>();
                List<Integer> userIDList = testDeviceList.stream().map(TestDevice::getOwnerid).collect(Collectors.toList());
                List<TestUser> selectedUsers = _userMapper.getByIDList(userIDList);
                for (TestDevice testDevice : testDeviceList) {
                    DTOGet_Devices_SharedDevice dtoGet_devices_sharedDevice = new DTOGet_Devices_SharedDevice();
                    dtoGet_devices_sharedDevice.setDeviceID(testDevice.getDeviceID());
                    dtoGet_devices_sharedDevice.setDeviceName(testDevice.getName());
                    for (TestUser testUser : selectedUsers) {
                        if (testUser.getId() == testDevice.getOwnerid()) {
                            dtoGet_devices_sharedDevice.setOwnerPhone(testUser.getPhone());
                        }
                    }
                    dtoGet_devices_sharedDevices.add(dtoGet_devices_sharedDevice);
                }
                dtoGet_devices.setSharedDevices(dtoGet_devices_sharedDevices);
            }
        }
        /*--------------log------------------*/
        logDto.setResponseData(JSONArray.toJSONString(dtoGet_devices));
        logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
        logger.info(JSONObject.toJSONString(logDto));
        /*--------------log------------------*/
        return successResult(dtoGet_devices);
    }

    @PostMapping("api/get_sharedList")
    public ResponseEntity<?> get_sharedList(@RequestBody DTOGet_SharedList dtoGetShared) {
        /*--------------log------------------*/
        DTOLogger logDto = new DTOLogger();
        logDto.setRequestTime(DateUtils.getDateTimeStr(new Date()));
        logDto.setAPIName("get_sharedList");
        logDto.setRequestUrl("api/get_sharedList");
        logDto.setPostData(JSONArray.toJSONString(dtoGetShared));
        /*--------------log------------------*/
        if (StringUtils.isEmpty(dtoGetShared.getPhone()) || StringUtils.isEmpty(dtoGetShared.getDeviceID())) {
            /*--------------log------------------*/
            logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
            logger.info(JSONObject.toJSONString(logDto));
        /*--------------log------------------*/
            return failResult(500.4, "请求缺少必要参数");
        }
        if (dtoGetShared.getPhone().length() != 11) {
            /*--------------log------------------*/
            logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
            logger.info(JSONObject.toJSONString(logDto));
        /*--------------log------------------*/
            return failResult(500.5, "手机号长度不合法");
        }
        TestUser _testUser = _userMapper.getByPhone(dtoGetShared.getPhone());
        if (_testUser == null) {
            /*--------------log------------------*/
            logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
            logger.info(JSONObject.toJSONString(logDto));
        /*--------------log------------------*/
            return failResult(500.1, "手机号不存在");
        }
        TestDevice _testDevice = _deviceMapper.getByDeviceID(dtoGetShared.getDeviceID());
        if (_testDevice == null) {
            /*--------------log------------------*/
            logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
            logger.info(JSONObject.toJSONString(logDto));
        /*--------------log------------------*/
            return failResult(500.2, "设备不存在");
        }
        if (_testDevice.getOwnerid() != _testUser.getId()) {
            /*--------------log------------------*/
            logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
            logger.info(JSONObject.toJSONString(logDto));
        /*--------------log------------------*/
            return failResult(500.3, "非该设备绑定者，不能执行 分享/取消 分享操作");
        }
        List<TestUserDevice> _testUserDeviceList = _userdeviceMapper.getByDeviceID(_testDevice.getId());
        List<TestUser> _testUserList = new ArrayList<>();
        List<RETPhoneInfo> _retPhoneInfos = new ArrayList<>();
        if (_testUserDeviceList != null && _testUserDeviceList.size() > 0) {
            List<Integer> UserIDList = _testUserDeviceList.stream().map(TestUserDevice::getUid).collect(Collectors.toList());
            _testUserList = _userMapper.getByIDList(UserIDList);
            if (_testUserList != null && _testUserList.size() > 0) {
                for (TestUser testUser : _testUserList) {
                    RETPhoneInfo retPhoneInfo = new RETPhoneInfo();
                    retPhoneInfo.setPhone(testUser.getPhone());
                    _retPhoneInfos.add(retPhoneInfo);
                }
            }
        }
        /*--------------log------------------*/
        logDto.setResponseData(JSONArray.toJSONString(_retPhoneInfos));
        logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
        logger.info(JSONObject.toJSONString(logDto));
        /*--------------log------------------*/
        return successResult(_retPhoneInfos);
    }

    public TestUser GetUser(String phone) {
        TestUser testUser = new TestUser();
        testUser.setPhone(phone);
        testUser.setHguid(UUID.randomUUID().toString().toLowerCase().trim().replaceAll("-", ""));
        testUser.setCreatedate(new Date());
        return testUser;
    }

    public TestDevice GetDevice(String deviceID, String Name) {
        TestDevice testDevice = new TestDevice();
        testDevice.setName(Name);
        testDevice.setDeviceID(deviceID);
        testDevice.setBluetoothkey(BluetoothUtils.blueToothKey);
        testDevice.setCreatedate(new Date());
        return testDevice;
    }

    public TestUserDevice GetUserDevice(int uid, int did) {
        TestUserDevice testUserDevice = new TestUserDevice();
        testUserDevice.setUid(uid);
        testUserDevice.setDid(did);
        testUserDevice.setCreatedate(new Date());
        return testUserDevice;
    }

    @PostMapping("api/add_fingerprint")
    public ResponseEntity<?> add_fingerprint(@RequestBody DTO_AddFingerprint dto) {
        /*--------------log------------------*/
        DTOLogger logDto = new DTOLogger();
        logDto.setRequestTime(DateUtils.getDateTimeStr(new Date()));
        logDto.setAPIName("add_fingerprint");
        logDto.setRequestUrl("api/add_fingerprint");
        logDto.setPostData(JSONArray.toJSONString(dto));
        /*--------------log------------------*/
        if (StringUtils.isEmpty(dto.getPhone()) || StringUtils.isEmpty(dto.getDeviceID()) || StringUtils.isEmpty(dto.getFingerprint())) {
            /*--------------log------------------*/
            logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
            logger.info(JSONObject.toJSONString(logDto));
            /*--------------log------------------*/
            return failResult(500.4, "请求缺少必要参数");
        }
        //判断phone的长度是否合理
        if (dto.getPhone().length() != 11) {
            /*--------------log------------------*/
            logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
            logger.info(JSONObject.toJSONString(logDto));
            /*--------------log------------------*/
            return failResult(500.5, "手机号长度不合法");
        }
        //判断手机号是否存在
        TestUser _testUser = _userMapper.getByPhone(dto.getPhone());
        if (_testUser == null) {
            /*--------------log------------------*/
            logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
            logger.info(JSONObject.toJSONString(logDto));
        /*--------------log------------------*/
            return failResult(500.1, "手机号不存在");
        }
        TestDevice _testDevice = _deviceMapper.getByDeviceID(dto.getDeviceID());
        if (_testDevice == null) {
            /*--------------log------------------*/
            logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
            logger.info(JSONObject.toJSONString(logDto));
        /*--------------log------------------*/
            return failResult(500.2, "设备不存在");
        }

        TestFingerprint entity = new TestFingerprint();
        entity.setGuid(UUID.randomUUID().toString().toLowerCase().trim().replaceAll("-", ""));
        entity.setCreatedate(new Date());
        entity.setPhone(dto.getPhone());
        entity.setDeviceID(dto.getDeviceID());
        entity.setFingerprint(dto.getFingerprint());
        entity.setFingerprintID(dto.getFingerprintID());
        entity.setFingerprintUID(dto.getFingerprintUID());
        int res = _fingerprintMapper.anyFingerprint(entity);
        if (res > 0) {
            _fingerprintMapper.update(entity);
        } else {
            _fingerprintMapper.insert(entity);
        }
        /*--------------log------------------*/
        logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
        logger.info(JSONObject.toJSONString(logDto));
        /*--------------log------------------*/
        return successResult(null);
    }


    @PostMapping("api/del_fingerprint")
    public ResponseEntity<?> del_fingerprint(@RequestBody DTO_DelFingerprint dto) {
        /*--------------log------------------*/
        DTOLogger logDto = new DTOLogger();
        logDto.setRequestTime(DateUtils.getDateTimeStr(new Date()));
        logDto.setAPIName("del_fingerprint");
        logDto.setRequestUrl("api/del_fingerprints");
        logDto.setPostData(JSONArray.toJSONString(dto));
        /*--------------log------------------*/
        int res = _fingerprintMapper.any(dto.getGuid(), dto.getPhone());
        if (res > 0) {
            _fingerprintMapper.del(dto.getGuid(), dto.getPhone());
        } else {
            /*--------------log------------------*/
            logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
            logger.info(JSONObject.toJSONString(logDto));
            /*--------------log------------------*/
            return failResult(500.7, "这条指纹不属于该用户");
        }
        /*--------------log------------------*/
        logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
        logger.info(JSONObject.toJSONString(logDto));
        /*--------------log------------------*/
        return successResult(null);
    }

    @PostMapping("api/add_unlockRecord")
    public ResponseEntity<?> add_unlockRecord(@RequestBody DTO_AddUnlockrecord dto) {
        /*--------------log------------------*/
        DTOLogger logDto = new DTOLogger();
        logDto.setRequestTime(DateUtils.getDateTimeStr(new Date()));
        logDto.setAPIName("add_unlockRecord");
        logDto.setRequestUrl("api/add_unlockRecord");
        logDto.setPostData(JSONArray.toJSONString(dto));
        /*--------------log------------------*/
        if (StringUtils.isEmpty(dto.getPhone()) || StringUtils.isEmpty(dto.getDeviceID()) || dto.getUnlockRecords() == null) {
            /*--------------log------------------*/
            logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
            logger.info(JSONObject.toJSONString(logDto));
            /*--------------log------------------*/
            return failResult(500.4, "请求缺少必要参数");
        }
        //判断phone的长度是否合理
        if (dto.getPhone().length() != 11) {
            /*--------------log------------------*/
            logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
            logger.info(JSONObject.toJSONString(logDto));
            /*--------------log------------------*/
            return failResult(500.5, "手机号长度不合法");
        }
        //判断手机号是否存在
        TestUser _testUser = _userMapper.getByPhone(dto.getPhone());
        if (_testUser == null) {
            /*--------------log------------------*/
            logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
            logger.info(JSONObject.toJSONString(logDto));
        /*--------------log------------------*/
            return failResult(500.1, "手机号不存在");
        }
        TestDevice _testDevice = _deviceMapper.getByDeviceID(dto.getDeviceID());
        if (_testDevice == null) {
            /*--------------log------------------*/
            logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
            logger.info(JSONObject.toJSONString(logDto));
        /*--------------log------------------*/
            return failResult(500.2, "设备不存在");
        }
        List<TestUnlockrecord> list = new ArrayList<TestUnlockrecord>();
        TestUnlockrecord entity = null;
        for (DTO_Unlockrecord item : dto.getUnlockRecords()) {
            entity = new TestUnlockrecord();
            entity.setGuid(UUID.randomUUID().toString().toLowerCase().trim().replaceAll("-", ""));
            entity.setPhone(dto.getPhone());
            entity.setDeviceID(dto.getDeviceID());
            entity.setType(item.getType());
            entity.setTimestamp(item.getTimestamp());
            if (1 == item.getType()) {
                if (StringUtils.isEmpty(item.getCode())) {
                    entity.setContent("");
                } else {
                    TestFingerprint entityFinger = _unlockrecordMapper.selectFingerprint(item.getCode());
                    if (entityFinger != null) {
                        entity.setContent(entityFinger.getFingerprint());
                    }
                }
            } else {
                entity.setContent(item.getCode());
            }
            list.add(entity);
        }

        _unlockrecordMapper.insertBatch(list);
        /*--------------log------------------*/
        logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
        logger.info(JSONObject.toJSONString(logDto));
        /*--------------log------------------*/
        return successResult(null);
    }

    @PostMapping("api/get_unlockRecord")
    public ResponseEntity<?> get_unlockRecord(@RequestBody DTO_GetUnlockrecord dto) {
        /*--------------log------------------*/
        DTOLogger logDto = new DTOLogger();
        logDto.setRequestTime(DateUtils.getDateTimeStr(new Date()));
        logDto.setAPIName("get_unlockRecord");
        logDto.setRequestUrl("api/get_unlockRecord");
        logDto.setPostData(JSONArray.toJSONString(dto));
        /*--------------log------------------*/
        if (StringUtils.isEmpty(dto.getPhone()) || StringUtils.isEmpty(dto.getDeviceID())) {
            /*--------------log------------------*/
            logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
            logger.info(JSONObject.toJSONString(logDto));
            /*--------------log------------------*/
            return failResult(500.4, "请求缺少必要参数");
        }
        //判断phone的长度是否合理
        if (dto.getPhone().length() != 11) {
            /*--------------log------------------*/
            logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
            logger.info(JSONObject.toJSONString(logDto));
            /*--------------log------------------*/
            return failResult(500.5, "手机号长度不合法");
        }
        //判断手机号是否存在
        TestUser _testUser = _userMapper.getByPhone(dto.getPhone());
        if (_testUser == null) {
            /*--------------log------------------*/
            logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
            logger.info(JSONObject.toJSONString(logDto));
        /*--------------log------------------*/
            return failResult(500.1, "手机号不存在");
        }
        TestDevice _testDevice = _deviceMapper.getByDeviceID(dto.getDeviceID());
        if (_testDevice == null) {
            /*--------------log------------------*/
            logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
            logger.info(JSONObject.toJSONString(logDto));
        /*--------------log------------------*/
            return failResult(500.2, "设备不存在");
        }

        int day = 0;
        Properties p = new Properties();
        try {
            InputStream in = new FileInputStream("conf/config.properties");
            p.load(in);
            day = Integer.parseInt(p.getProperty("day"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        List<DTO_ResUnlockrecord> resList = new ArrayList<DTO_ResUnlockrecord>();
        DTO_ResUnlockrecord resDto = null;
        List<TestUnlockrecord> list = _unlockrecordMapper.select(dto.getDeviceID(), dto.getPhone(), DateUtils.forwardNow(day));

        for (TestUnlockrecord entity : list) {
            resDto = new DTO_ResUnlockrecord();
            BeanUtils.copyProperties(entity, resDto);
            resList.add(resDto);
        }

        /*--------------log------------------*/
        logDto.setResponseData(JSONArray.toJSONString(resList));
        logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
        logger.info(JSONObject.toJSONString(logDto));
        /*--------------log------------------*/
        return successResult(resList);

}


    public static void main(String[] args) {

        Properties p = new Properties();
        try {
            InputStream in = new FileInputStream("conf/config.properties");
            p.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(p.getProperty("day"));
//        JSONObject o = new JSONObject();
//        o.put("A","value");
//        System.out.println(o);
//        logger.error("111");
//        logger.info("222");
//        logger.debug("333");
//        List<DTO_Fingerprint> list = new ArrayList<DTO_Fingerprint>();
//        DTO_Fingerprint fDto = new DTO_Fingerprint();
//        fDto.setGuid("1");
//        fDto.setContent("2");
//        fDto.setCreatedate("3");
//        list.add(fDto);
//
//        TestFingerprint test = new TestFingerprint();
//
//        BeanUtils.copyProperties(fDto, test);
//        String bbb = JSONArray.toJSONString(fDto);
//        String jsonString = JSONArray.toJSONString(list);
//        JSONArray jsonArray = JSONArray.parseArray(jsonString);
//
//        String a = "[{\"studentName\":\"lily\",\"studentAge\":12}]";
//        System.out.println(bbb);
//        System.out.println(jsonString);
//        System.out.println(jsonArray);
        // System.out.println(jsonArray);
        //System.out.println(JSONObject.parseObject(a));


    }
}
