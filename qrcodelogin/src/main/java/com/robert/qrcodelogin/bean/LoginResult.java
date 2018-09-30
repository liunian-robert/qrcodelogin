package com.robert.qrcodelogin.bean;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;


/**
 * @创建人: zhangyapo
 * @创建时间: 2018/9/30 16:38
 * @描述:
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
