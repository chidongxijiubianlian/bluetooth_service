package org.andon.bluetooth_service.controller;

import org.andon.bluetooth_service.base.BaseController;
import org.andon.bluetooth_service.base.ResponseEntity;
import org.andon.bluetooth_service.common.BluetoothUtils;
import org.andon.bluetooth_service.dto.*;
import org.andon.bluetooth_service.entity.TestDevice;
import org.andon.bluetooth_service.entity.TestUser;
import org.andon.bluetooth_service.entity.TestUserDevice;
import org.andon.bluetooth_service.mapper.DeviceMapper;
import org.andon.bluetooth_service.mapper.UserMapper;
import org.andon.bluetooth_service.mapper.UserDeviceMapper;
import org.andon.bluetooth_service.ret.RETPhoneInfo;
import org.andon.bluetooth_service.ret.RETSharedDeviceToUsers;
import org.andon.bluetooth_service.ret.RETSharedUser;
import org.andon.bluetooth_service.service.IDeviceUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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
    @PostMapping("api/bind_bluetooth")
    public ResponseEntity<?> bind_bluetooth(@RequestBody DTO_BindBluetooth dto_bindBluetooth)
    {
        if(StringUtils.isEmpty(dto_bindBluetooth.getPhone()) ||StringUtils.isEmpty(dto_bindBluetooth.getDeviceID())||StringUtils.isEmpty(dto_bindBluetooth.getDeviceName()))
        {
            return failResult(500.4,"请求缺少必要参数");
        }
        //判断phone的长度是否合理
        if( dto_bindBluetooth.getPhone().length() !=11)
        {
            return failResult(500.5,"手机号长度不合法");
        }
        //判断手机号是否存在
        TestUser  _testUser = _userMapper.getByPhone(dto_bindBluetooth.getPhone());
        if(_testUser ==null)
        {
            _testUser = GetUser(dto_bindBluetooth.getPhone());
            _userMapper.insert(_testUser);
        }
        TestDevice _testDevice = _deviceMapper.getByDeviceID(dto_bindBluetooth.getDeviceID());
        if(_testDevice ==null)
        {
            _testDevice =GetDevice(dto_bindBluetooth.getDeviceID(),dto_bindBluetooth.getDeviceName());
            _deviceMapper.insert(_testDevice);
        }
        if(_testDevice.getOwnerid() !=0 && _testDevice.getOwnerid() !=_testUser.getId())
        {
            return failResult(500.6,"设备已绑定");
        }
        if(!_testDevice.getName().equals(dto_bindBluetooth.getDeviceName()))
        {
            _testDevice.setName(dto_bindBluetooth.getDeviceName());
        }
        _testDevice.setOwnerid(_testUser.getId());
        _deviceMapper.update(_testDevice);
        //先查询 手机号是否存在
        return successResult(null);
    }
    @PostMapping("api/share_bluetooth")
    public ResponseEntity<?> share_bluetooth(@RequestBody DTOShareBluetooth dtoShareBluetooth)
    {
        if(StringUtils.isEmpty(dtoShareBluetooth.getPhone()) ||StringUtils.isEmpty(dtoShareBluetooth.getDeviceID())||StringUtils.isEmpty(dtoShareBluetooth.getSharedPhone()))
        {
            return failResult(500.4,"请求缺少必要参数");
        }
        if( dtoShareBluetooth.getSharedPhone().length() !=11)
        {
            return failResult(500.5,"手机号长度不合法");
        }
        TestUser  _testUser = _userMapper.getByPhone(dtoShareBluetooth.getPhone());
        if(_testUser ==null)
        {
            return failResult(500.1,"手机号不存在");
        }
        TestDevice _testDevice = _deviceMapper.getByDeviceID(dtoShareBluetooth.getDeviceID());
        if(_testDevice ==null)
        {
            return failResult(500.2,"设备不存在");
        }
        if(_testDevice.getOwnerid() !=_testUser.getId())
        {
            return failResult(500.3,"非该设备绑定者，不能执行 分享/取消 分享操作");
        }
        TestUser  _sharedUser = _userMapper.getByPhone(dtoShareBluetooth.getSharedPhone());
        if(_sharedUser ==null)
        {
            _sharedUser = GetUser(dtoShareBluetooth.getSharedPhone());
            _userMapper.insert(_sharedUser);
        }
        TestUserDevice testUserDevice =_userdeviceMapper.getByID(_sharedUser.getId(),_testDevice.getId());
        if(testUserDevice ==null)
        {
            testUserDevice =GetUserDevice(_sharedUser.getId(),_testDevice.getId());
            _userdeviceMapper.insert(testUserDevice);
        }
        RETSharedDeviceToUsers retSharedDeviceToUsers = new RETSharedDeviceToUsers();
        retSharedDeviceToUsers =_ideviceUsers.GetDeviceUsers(dtoShareBluetooth.getDeviceID());
        return successResult(retSharedDeviceToUsers);
    }
    @PostMapping("api/unshare_bluetooth")
    public ResponseEntity<?> unshare_bluetooth(@RequestBody DTOUnShareBluetooth dtoUnShareBluetooth)
    {
        if(StringUtils.isEmpty(dtoUnShareBluetooth.getPhone()) ||StringUtils.isEmpty(dtoUnShareBluetooth.getDeviceID())||StringUtils.isEmpty(dtoUnShareBluetooth.getUnSharedPhone()))
        {
            return failResult(500.4,"请求缺少必要参数");
        }
        TestUser  _testUser = _userMapper.getByPhone(dtoUnShareBluetooth.getPhone());
        if(_testUser ==null)
        {
            return failResult(500.1,"手机号不存在");
        }
        TestDevice _testDevice = _deviceMapper.getByDeviceID(dtoUnShareBluetooth.getDeviceID());
        if(_testDevice ==null)
        {
            return failResult(500.2,"设备不存在");
        }
        if(_testDevice.getOwnerid() !=_testUser.getId())
        {
            return failResult(500.3,"非该设备绑定者，不能执行 分享/取消 分享操作");
        }
        TestUser  _sharedUser = _userMapper.getByPhone(dtoUnShareBluetooth.getUnSharedPhone());
        if(_sharedUser ==null)
        {
            _sharedUser = GetUser(dtoUnShareBluetooth.getUnSharedPhone());
            _userMapper.insert(_sharedUser);
        }
        TestUserDevice testUserDevice =_userdeviceMapper.getByID(_sharedUser.getId(),_testDevice.getId());
        if(testUserDevice !=null)
        {
            _userdeviceMapper.del(testUserDevice.getId());
        }
        RETSharedDeviceToUsers sharedUser = new RETSharedDeviceToUsers();
        sharedUser =_ideviceUsers.GetDeviceUsers(dtoUnShareBluetooth.getDeviceID());
        return successResult(sharedUser);
    }
    @PostMapping("api/get_devices")
    public ResponseEntity<?> get_devices(@RequestBody DTOMy_Devices dtoMy_devices)
    {
        if(StringUtils.isEmpty(dtoMy_devices.getPhone()))
        {
            return failResult(500.4,"请求缺少必要参数");
        }
        if(dtoMy_devices.getPhone().length() !=11)
        {
            return failResult(500.5,"手机号长度不合法");
        }
        TestUser  _testUser = _userMapper.getByPhone(dtoMy_devices.getPhone());
        if(_testUser ==null)
        {
            _testUser = GetUser(dtoMy_devices.getPhone());
            _userMapper.insert(_testUser);
        }
        List<TestDevice> testDevicesList =_deviceMapper.getByOwnerID(_testUser.getId());
        List<TestUserDevice> testUserDeviceList =_userdeviceMapper.getByUserID(_testUser.getId());
        DTOGet_Devices dtoGet_devices =new DTOGet_Devices();
        dtoGet_devices.setDevices(new ArrayList<DTOGet_Devices_MyDevice>());
        dtoGet_devices.setSharedDevices(new ArrayList<DTOGet_Devices_SharedDevice>());
        if(testDevicesList !=null && testDevicesList.size()>0)
        {
            List<DTOGet_Devices_MyDevice> dtoGet_devices_myDevices = new ArrayList<>();
            for(TestDevice device:testDevicesList)
            {
                DTOGet_Devices_MyDevice myDevice = new DTOGet_Devices_MyDevice();
                myDevice.setDeviceID(device.getDeviceID());
                myDevice.setDeviceName(device.getName());
                dtoGet_devices_myDevices.add(myDevice);
            }
            dtoGet_devices.setDevices(dtoGet_devices_myDevices);
        }
        if(testUserDeviceList !=null && testUserDeviceList.size()>0)
        {
            List<DTOGet_Devices_SharedDevice> dtoGet_devices_myDevices = new ArrayList<>();
            List<Integer> didList =testUserDeviceList.stream().map(TestUserDevice::getDid).collect(Collectors.toList());
            List<TestDevice> testDeviceList =_deviceMapper.getByDids(didList);
            if(testDeviceList !=null && testDeviceList.size()>0)
            {
                List<DTOGet_Devices_SharedDevice> dtoGet_devices_sharedDevices = new ArrayList<>();
                List<Integer> userIDList =testDeviceList.stream().map(TestDevice::getOwnerid).collect(Collectors.toList());
                List<TestUser> selectedUsers = _userMapper.getByIDList(userIDList);
                for (TestDevice testDevice:testDeviceList)
                {
                    DTOGet_Devices_SharedDevice dtoGet_devices_sharedDevice = new DTOGet_Devices_SharedDevice();
                    dtoGet_devices_sharedDevice.setDeviceID(testDevice.getDeviceID());
                    dtoGet_devices_sharedDevice.setDeviceName(testDevice.getName());
                    for (TestUser testUser:selectedUsers)
                    {
                        if(testUser.getId() ==testDevice.getOwnerid())
                        {
                            dtoGet_devices_sharedDevice.setOwnerPhone(testUser.getPhone());
                        }
                    }
                    dtoGet_devices_sharedDevices.add(dtoGet_devices_sharedDevice);
                }
                dtoGet_devices.setSharedDevices(dtoGet_devices_sharedDevices);
            }
        }
        return successResult(dtoGet_devices);
    }
    @PostMapping("api/get_sharedList")
    public ResponseEntity<?> get_sharedList(@RequestBody DTOGet_SharedList dtoGetShared)
    {
        if(StringUtils.isEmpty(dtoGetShared.getPhone()) || StringUtils.isEmpty(dtoGetShared.getDeviceID()))
        {
            return failResult(500.4,"请求缺少必要参数");
        }
        if(dtoGetShared.getPhone().length() !=11)
        {
            return failResult(500.5,"手机号长度不合法");
        }
        TestUser  _testUser = _userMapper.getByPhone(dtoGetShared.getPhone());
        if(_testUser ==null)
        {
            return failResult(500.1,"手机号不存在");
        }
        TestDevice _testDevice =_deviceMapper.getByDeviceID(dtoGetShared.getDeviceID());
        if(_testDevice ==null)
        {
            return failResult(500.2,"设备不存在");
        }
        if(_testDevice.getOwnerid() !=_testUser.getId())
        {
            return failResult(500.3,"非该设备绑定者，不能执行 分享/取消 分享操作");
        }
        List<TestUserDevice> _testUserDeviceList =_userdeviceMapper.getByDeviceID(_testDevice.getId());
        List<TestUser> _testUserList =new ArrayList<>();
        List<RETPhoneInfo> _retPhoneInfos =new ArrayList<>();
        if(_testUserDeviceList !=null && _testUserDeviceList.size()>0)
        {
            List<Integer> UserIDList =_testUserDeviceList.stream().map(TestUserDevice::getUid).collect(Collectors.toList());
            _testUserList =_userMapper.getByIDList(UserIDList);
            if(_testUserList !=null && _testUserList.size()>0)
            {
                for (TestUser testUser:_testUserList)
                {
                    RETPhoneInfo retPhoneInfo = new RETPhoneInfo();
                    retPhoneInfo.setPhone(testUser.getPhone());
                    _retPhoneInfos.add(retPhoneInfo);
                }
            }
        }
        return successResult(_retPhoneInfos);
    }
    public TestUser GetUser(String phone)
    {
        TestUser testUser = new TestUser();
        testUser.setPhone(phone);
        testUser.setHguid(UUID.randomUUID().toString().toLowerCase().trim().replaceAll("-", ""));
        testUser.setCreatedate(new Date());
        return testUser;
    }
    public TestDevice GetDevice(String deviceID,String Name)
    {
        TestDevice testDevice = new TestDevice();
        testDevice.setName(Name);
        testDevice.setDeviceID(deviceID);
        testDevice.setBluetoothkey(BluetoothUtils.blueToothKey);
        testDevice.setCreatedate(new Date());
        return testDevice;
    }
    public TestUserDevice GetUserDevice(int uid,int did)
    {
        TestUserDevice testUserDevice = new TestUserDevice();
        testUserDevice.setUid(uid);
        testUserDevice.setDid(did);
        testUserDevice.setCreatedate(new Date());
        return testUserDevice;
    }
}
