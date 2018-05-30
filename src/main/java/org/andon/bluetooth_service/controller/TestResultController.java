package org.andon.bluetooth_service.controller;

import org.andon.bluetooth_service.base.BaseController;
import org.andon.bluetooth_service.base.ResponseEntity;
import org.andon.bluetooth_service.dto.TestResult.DTOTestPost;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class TestResultController extends BaseController {
    @PostMapping("api/test/TestPost")
    public ResponseEntity<?> TestPost(@RequestBody DTOTestPost dtoTestPost)
    {
        return successResult(dtoTestPost);
    }
}
