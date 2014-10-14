package io.terminus.snz.eai.dto;

import com.google.common.collect.Lists;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Date: 7/30/14
 * Time: 13:04
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */

@XStreamAlias("OUTPUT")
public class InfoSelectedFromMDM<T> implements Serializable {
    private static final long serialVersionUID = -6758460285879826345L;

    @Getter
    @Setter
    private List<T> ROWSET = Lists.<T>newArrayList();
}
