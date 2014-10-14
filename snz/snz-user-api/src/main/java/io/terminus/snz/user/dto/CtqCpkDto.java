package io.terminus.snz.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 7/7/14
 */
public class CtqCpkDto implements Serializable {


    private static final long serialVersionUID = 9123654158889590305L;

    @Getter
    @Setter
    private String ctq;

    @Getter
    @Setter
    private String cpk;
}
