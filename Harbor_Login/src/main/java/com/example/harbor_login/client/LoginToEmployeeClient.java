package com.example.harbor_login.client;


import com.example.harbor_login.Login.dto.response.LoginMemberResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "Employee", url = "http://localhost:8004")
public interface LoginToEmployeeClient {
    @PostMapping("employee/create")
    HttpStatus createEmployee(@RequestBody LoginMemberResDto request);
}
