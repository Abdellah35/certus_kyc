package com.softedge.solution.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(name = "certus-user-service")
public interface CertusUserServiceProxy {


    @GetMapping("/greet")
    public String greet();

    @GetMapping("/wish")
    public String wish();

}