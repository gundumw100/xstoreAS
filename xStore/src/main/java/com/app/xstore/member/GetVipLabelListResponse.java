package com.app.xstore.member;

import com.app.model.response.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2018/10/23.
 */

public class GetVipLabelListResponse extends BaseResponse {
    List<VipLabel> Info;

    public List<VipLabel> getInfo() {
        return Info;
    }

    public void setInfo(List<VipLabel> info) {
        Info = info;
    }
}
