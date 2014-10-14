package io.terminus.snz.requirement.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Author:  <a href="mailto:i@terminus.io">jlchen</a>
 * Date: 2014-09-15
 */
public class SolutionFileDto implements Serializable {
    private static final long serialVersionUID = -4548065020264882585L;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String url;

    @Getter
    @Setter
    private String version; //yyyy-MM-dd HH:mm:ss 格式
}
