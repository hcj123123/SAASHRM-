package com.ihrm.system.client;


import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.domain.company.Department;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "ihrm-company")
public interface DepartmentFeignClient {

    @RequestMapping(value = "/company/department/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable(value = "id")String id);
}
