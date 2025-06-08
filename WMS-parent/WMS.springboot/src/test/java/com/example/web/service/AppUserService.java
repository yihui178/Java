package com.example.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.web.dto.AppUserDto;
import com.example.web.dto.query.AppUserPagedInput;
import com.example.web.entity.AppUser;
import com.example.web.tools.dto.IdInput;
import com.example.web.tools.dto.IdsInput;
import com.example.web.tools.dto.PagedResult;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

/**
 * 用户功能的Service接口的定义清单
 */
public interface AppUserService extends IService<AppUser> {

    /**
     * 用户的分页查询方法接口定义
     */
    public PagedResult<AppUserDto> List(AppUserPagedInput input);

    /**
     * 用户的新增或者修改方法接口定义
     */
    public AppUserDto CreateOrEdit( AppUserDto input);

    /**
     * 用户模块删除
     */
    public void Delete(IdInput input);

    /**
     * 用户模块批量删除
     */
    public void BatchDelete(IdsInput input);


    /**
     * 查询单个对象
     */
    public AppUserDto Get(AppUserPagedInput input);

    /**
     * 用户登录
     */
    public String SignIn(AppUserDto input);

    /**
     * 注册
     */
    public AppUserDto Register(AppUserDto input);

    /**
     * 找回密码
     */
    void ForgetPassword(AppUserDto input);

    /**
     * 用户导出
     */
    public void Export(@RequestParam String query, HttpServletResponse response) throws IOException;

}
