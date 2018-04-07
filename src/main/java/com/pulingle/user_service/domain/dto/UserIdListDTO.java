package com.pulingle.user_service.domain.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by @杨健 on 2018/4/7 17:59
 *
 * @Des: 用户Id列表传输对象类
 */

public class UserIdListDTO implements Serializable {
    private List<String> idList;

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }
}
