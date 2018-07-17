package com.robert.qrcodelogin.service.impl;

import com.robert.qrcodelogin.bean.LoginResult;
import com.robert.qrcodelogin.bean.User;
import com.robert.qrcodelogin.service.UserLoginService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @创建人: zhangyapo
 * @创建时间: 2018/7/12 17:21
 * @描述:二维码扫描无密码登录逻辑
 */
@Service("userLoginService")
public class UserLoginServiceImpl implements UserLoginService {

    @Override
    public List<Integer> login(User user) throws Exception {
        //无密码登录逻辑
        return new ArrayList<Integer>();
    }
}
