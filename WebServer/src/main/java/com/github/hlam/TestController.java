package com.github.hlam;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @PostMapping
    public TestObject testMethod() {
        return new TestObject("OK");
    }
}
