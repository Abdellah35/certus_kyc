package com.softedge.solution.proxy;

import com.softedge.solution.feignbeans.UserIPVActivation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "certus-redis-cache")
public interface CertusRedisCacheServiceProxy {

    @GetMapping("/redis/user")
    public String hello();

    @PostMapping("/redis/user-ipv")
    public UserIPVActivation add(UserIPVActivation userIPVActivation);

    @GetMapping("/redis/user-ipv/")
    public UserIPVActivation findById(@RequestParam("email-id") String emailId);
}
