package com.robert.qrcodelogin.bean;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: qijun
 * Email: 18353367683@163.com
 * Date: 2017-10-12 13:44
 * Version: 1.0.0
 * Description:登录成功后返回的数据
 */
public class LoginResult {

    @ApiModelProperty("通行证账号")
    private String passport;

    @ApiModelProperty("企业id列表")
    private List<Integer> enterpriseIdList;


    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public List<Integer> getEnterpriseIdList() {
        return enterpriseIdList;
    }

    public void setEnterpriseIdList(List<Integer> enterpriseIdList) {
        this.enterpriseIdList = enterpriseIdList;
    }
}
