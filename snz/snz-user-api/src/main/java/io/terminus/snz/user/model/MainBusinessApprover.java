package io.terminus.snz.user.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;

/**
 * Author:Guo Chaopeng
 * Created on 14-7-31.
 */
@Repository
public class MainBusinessApprover implements Serializable {

    private static final long serialVersionUID = 3227277796252394302L;

    @Setter
    @Getter
    private Long id;

    @Setter
    @Getter
    private String memberName;

    @Setter
    @Getter
    private String memberId;

    @Setter
    @Getter
    private String leaderName;

    @Setter
    @Getter
    private String leaderId;

    @Setter
    @Getter
    private Long mainBusinessId;

    @Setter
    @Getter
    private String mainBusinessName;

    @Setter
    @Getter
    private Date createdAt;

    @Setter
    @Getter
    private Date updatedAt;

}
