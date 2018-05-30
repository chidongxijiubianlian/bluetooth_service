package org.andon.bluetooth_service.base;

import org.andon.bluetooth_service.mapper.DeviceMapper;
import org.andon.bluetooth_service.mapper.UserDeviceMapper;
import org.andon.bluetooth_service.mapper.UserMapper;
import org.andon.bluetooth_service.service.IDeviceUsers;

public class BaseController {

    public <T> ResponseEntity<T> successResult(T entity)
    {
        return new ResponseEntity(entity);
    }
    public <T> ResponseEntity<T> failResult(double code,String message)
    {
        return new ResponseEntity(code,message);
    }
}
