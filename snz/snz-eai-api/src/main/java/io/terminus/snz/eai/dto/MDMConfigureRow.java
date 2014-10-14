package io.terminus.snz.eai.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Date: 7/30/14
 * Time: 13:14
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */

@ToString
@NoArgsConstructor
@XStreamAlias("row")
public class MDMConfigureRow implements Serializable {
    private static final long serialVersionUID = -4616538268883486682L;

    @Getter
    @Setter
    private String VALUE;           // code

    @Getter
    @Setter
    private String VALUE_SET_ID;       // type

    @Getter
    @Setter
    private String PARENT_VALUE_LOW;   // parent

    @Getter
    @Setter
    private String VALUE_MEANING;      // name

    @Getter
    @Setter
    private String DELETE_FLAG;        // delete flag
}
