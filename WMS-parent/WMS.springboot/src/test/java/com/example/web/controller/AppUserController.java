package com.example.web.controller;

import com.example.web.dto.AppUserDto;
import com.example.web.dto.query.AppUserPagedInput;
import com.example.web.service.AppUserService;
import com.example.web.tools.BaseContext;
import com.example.web.tools.dto.IdInput;
import com.example.web.tools.dto.IdsInput;
import com.example.web.tools.dto.PagedResult;
import com.example.web.tools.dto.ResponseData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


/**
 * 用户控制器
 */
@RestController()
@RequestMapping("/User")
public class AppUserController {
    @Autowired()
    private  AppUserService AppUserService;


    /**
     * 用户分页查询接口
     */
    @RequestMapping(value = "/List", method = RequestMethod.POST)
    @SneakyThrows
    public PagedResult<AppUserDto> List(@RequestBody AppUserPagedInput input) {

        return AppUserService.List(input);
    }

    /**
     * 用户创建或则修改接口
     */
   @RequestMapping(value = "/CreateOrEdit", method = RequestMethod.POST)
    public AppUserDto CreateOrEdit(@RequestBody AppUserDto input)  {
        return AppUserService.CreateOrEdit(input);

    }
    /**
     * 用户删除
     */
    @RequestMapping(value = "/Delete", method = RequestMethod.POST)
    public void Delete(@RequestBody IdInput input)
    {
        AppUserService.Delete(input);
    }

    /**
     * 用户批量删除
     */
    @RequestMapping(value = "/BatchDelete", method = RequestMethod.POST)
    public void BatchDelete(@RequestBody IdsInput input)
    {
        AppUserService.BatchDelete(input);
    }


    /**
     * 查询单个对用户
     */
    @RequestMapping(value = "/Get", method = RequestMethod.POST)
    public AppUserDto Get(@RequestBody AppUserPagedInput input)
    {
        return AppUserService.Get(input);
    }

    /**
     * 用户登录
     */
    @RequestMapping(value = "/SignIn", method = RequestMethod.POST)
    public ResponseData<String> SignIn(@RequestBody AppUserDto input, HttpServletRequest request)
    {
        String token= AppUserService.SignIn(input);
        return ResponseData.GetResponseDataInstance(token,"登录成功",true);
    }

    /**
     * 获取用户信息
     */
    @SneakyThrows
    @RequestMapping(value = "/GetByToken", method = RequestMethod.POST)
    public AppUserDto GetByToken(@RequestHeader("Authorization") String token){

        Integer userId= BaseContext.getCurrentUserDto().getUserId();
        AppUserPagedInput queryInput=new AppUserPagedInput();
        queryInput.setId(userId);
        AppUserDto AppUserDto=AppUserService.Get(queryInput);

        return AppUserDto;
    }

    /**
     * 用户注册接口
     */
    @RequestMapping(value = "/Register", method = RequestMethod.POST)
    public AppUserDto Register(@RequestBody AppUserDto input) throws Exception {

        return AppUserService.Register(input);

    }
  /**
     * 找回密码
     */
    @RequestMapping(value = "/ForgetPassword", method = RequestMethod.POST)
    public void ForgetPassword(@RequestBody AppUserDto input) throws Exception {
        AppUserService.ForgetPassword(input);
    }
    /**
     * 用户导出
     */
    @RequestMapping(value = "/Export", method = RequestMethod.GET)
    public void Export(@RequestParam String query, HttpServletResponse response) throws IOException {
        AppUserService.Export(query,response);
    }
}
