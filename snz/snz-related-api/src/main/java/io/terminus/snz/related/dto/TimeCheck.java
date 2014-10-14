package io.terminus.snz.related.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Author:  wenhaoli
 * Date: 2014-08-20
 */
public class TimeCheck implements Serializable {
    private static final long serialVersionUID = 7432275120947054644L;

    @Getter
    @Setter
    private Date starts;

    @Getter
    @Setter
    private Date starte;

    @Getter
    @Setter
    private Date ends;

    @Getter
    @Setter
    private Date ende;

}
