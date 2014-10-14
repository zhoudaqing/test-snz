package io.terminus.snz.requirement.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Desc:后台类目的JSON解析
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-09-02.
 */
public class BackendJSON implements Serializable {
    private static final long serialVersionUID = -5981348237378940857L;

    @Setter
    @Getter
    private Long bcId;  //后台类目编号

    @Setter
    @Getter
    private String name;//后台类目名称
}
