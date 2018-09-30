package com.robert.qrcodelogin.service;

import com.robert.qrcodelogin.bean.LoginResult;
import com.robert.qrcodelogin.bean.User;

import java.util.List;

/**
 * @创建人: zhangyapo
 * @创建时间: 2018/7/12 17:12
 * @描述:
 */
public interface UserLoginService {

    List<Integer> login(User user) throws Exception;
}
