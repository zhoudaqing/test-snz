package io.terminus.snz.user.event;

/**
 * Author:Guo Chaopeng
 * Created on 14-6-27.
 */
public class ApproveEvent {

    private ApproveResult approveResult;     //审核结果

    private String approver;                 //审核人

    private String mobile;                   //审核人联系方式

    private String description;              //审批意见

    private Long senderId;

    private Long receiverId;

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public ApproveResult getApproveResult() {
        return approveResult;
    }

    public void setApproveResult(ApproveResult approveResult) {
        this.approveResult = approveResult;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public static enum ApproveResult {
        ENTER_FAIL,        //入驻审核不通过
        MODIFY_INFO_FAIL,  //修改信息审核不通过
        ENTER_OK,          //入驻审核通过
        MODIFY_INFO_OK;    //修改信息审核通过
    }

}
