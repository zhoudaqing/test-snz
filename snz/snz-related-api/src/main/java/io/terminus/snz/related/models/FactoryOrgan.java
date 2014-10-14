package io.terminus.snz.related.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 工厂组织对应关系
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-7-25
 */
@ToString @NoArgsConstructor
public class FactoryOrgan implements Serializable{
    private static final long serialVersionUID = 4123696579711521689L;

    @Getter @Setter
    private Long id;

    @Getter @Setter
    private String factory;

    @Getter @Setter
    private String organ;

    public FactoryOrgan(String factory, String organ){
        this.factory = factory;
        this.organ = organ;
    }

    @Getter @Setter
    private Date createdAt;

    @Getter @Setter
    private Date updatedAt;

}
