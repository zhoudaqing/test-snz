package io.terminus.snz.requirement.service;

import com.google.common.base.Objects;
import io.terminus.common.model.Paging;
import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dto.RequirementDetailDto;
import io.terminus.snz.requirement.dto.RequirementDto;
import io.terminus.snz.requirement.dto.TopicUser;
import io.terminus.snz.requirement.model.Requirement;
import io.terminus.snz.requirement.model.RequirementTeam;

import java.util.List;

/**
 * Desc:采购商需求处理服务
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-03.
 */
public interface RequirementService {
    public static enum QueryType {
        MY_CREATE(1, "创建的需求"),
        MY_JOIN(2, "参与的需求"),
        MY_AUDIT(3, "审核的需求");

        private final int value;

        private final String display;

        private QueryType(int value, String display) {
            this.value = value;
            this.display = display;
        }

        public static QueryType from(int value) {
            for(QueryType queryType : QueryType.values()) {
                if(Objects.equal(queryType.value, value)) {
                    return queryType;
                }
            }
            return null;
        }

        public int value() {
            return value;
        }

        public String toString() {
            return display;
        }
    }

    /**
     * 创建需求信息
     * @param requirementDto 详细需求信息
     * @param user 创建需求的创建者的信息（user是容器自动注入的）
     * @return Long
     * 返回需求编号
     */
    @Export(paramNames = {"requirementDto" , "user"})
    public Response<Long> create(RequirementDto requirementDto , BaseUser user);

    /**
     * 通过共采购商编号&需求名称判断该需求是否已存在
     * @param purchaserId   采购商编号
     * @param name          需求名称
     * @return  Boolean
     * 返回是否存在
     */
    @Export(paramNames = {"purchaserId", "name"})
    public Response<Boolean> existName(Long purchaserId, String name);

    /**
     * 更新需求信息
     * @param requirementDto 需求信息
     * @return Boolean
     * 返回更新结果
     * (这个操作只有当前需求处于未锁定状态时才能更改,其中模块的更改决定使用异步处理。)
     */
    @Export(paramNames = {"requirementDto"})
    public Response<Boolean> update(RequirementDto requirementDto);

    /**
     * 更新需求上传的文件
     * @param requirementId 需求的编号
     * @param accessories 上传的文件
     * @return
     * 是否更新成功
     */
    @Export(paramNames = {"requirementId","accessories"})
    public Response<Boolean> updateAccessories(Long requirementId, String accessories);

    /**
     * 根据需求编号获取需求下的所有团队成员
     * @param requirementId 需求编号
     * @return  List
     * 返回需求团队详细信息
     */
    @Export(paramNames = {"requirementId"})
    public Response<List<RequirementTeam>> findRequirementTeam(Long requirementId);

    /**
     * 删除需求中的团队人员
     * @param teamId  团队人员编号
     * @return  Boolean
     * 返回删除是否成功
     */
    @Export(paramNames = {"teamId"})
    public Response<Boolean> deleteTeam(Long teamId);

    /**
     * 通过需求编号删除需求信息（逻辑删除）
     * @param requirementId 需求信息编号
     * @return  Boolean
     * 返回删除结果信息
     */
    @Export(paramNames = {"requirementId"})
    public Response<Boolean> delete(Long requirementId);

    /**
     * 通过需求编号查询详细的需求信息
     * @param requirementId 需求信息编号
     * @return  Requirement
     * 返回详细的需求信息
     */
    @Export(paramNames = {"requirementId"})
    public Response<RequirementDto> findById(Long requirementId);

    /**
     * 通过需求编号查询详细的需求信息内容（需求信息&模块信息等）
     * @param requirementId 需求编号
     * @return RequirementDetailDto
     * 返回详细的需求信息内容
     */
    @Export(paramNames = {"requirementId" , "user"})
    public Response<RequirementDetailDto> findDetailById(Long requirementId , BaseUser user);

    /**
     * 查询采购商的需求信息（需要分页处理）
     * @param user      采购商用户
     * @param queryType 查询类型（1:查询自己创建的需求，2:查询作为团队成员参与的需求，3:查询需要审核的需求）
     * @param status    需求状态（null：显示除删除状态的全部数据, -5:未提交审核,-4:待审核,-3:审核通过,-2:审核不通过,
     *                  -1:删除状态，0:等待发布，1:需求交互，2:需求锁定，3:方案交互，4:方案综投，5:选定供应商与方案，6:招标结束）
     * @param reqName   需求名称的模糊查询
     * @param startAt   开始时间
     * @param endAt     结束时间
     * @param pageNo    当前页数（默认为0）
     * @param size      分页大小（默认为20）
     * @return Paging
     */
    @Export(paramNames = {"user", "queryType", "status", "reqName", "startAt", "endAt", "pageNo", "size"})
    public Response<Paging<Requirement>> findByPurchaser(BaseUser user, Integer queryType, Integer status, String reqName,
                                                         String startAt, String endAt, Integer pageNo , Integer size);

    /**
     * 查询需求信息（需要分页处理）
     * @param frontId 前台类目编号（用于获取后台的类目编号关系－》然后查找对应的类目下的需求）
     * @param status  需求状态（null：显示除删除状态的全部数据）
     * @param pageNo  当前页数（默认为0）
     * @param size    分页大小（默认为20）
     * @return Paging
     * 返回分页数据信息
     */
    @Export(paramNames = {"frontId", "status", "pageNo","size"})
    public Response<Paging<Requirement>> findByParams(Long frontId, Integer status, Integer pageNo ,
                                                         Integer size);

    /**
     * 根据需求编号发送提交审核请求
     * @param requirementId 需求编号
     * @param user          用户信息
     * @return  String
     * 返回发送的上级的名称
     */
    @Export(paramNames = {"requirementId" , "user"})
    public Response<String> askAudit(Long requirementId , BaseUser user);

    /**
     * 通过审核人员编号&需求编号，由审核人员对提交的需求进行审核操作。
     * @param requirementId 需求编号
     * @param auditRes      审核结果
     * @param user          审核人员对象
     * @return Boolean
     * 返回处理结果
     * （这个还需要判断这个auditor是否是需求提交者的上级领导）
     */
    @Export(paramNames = {"requirementId", "auditRes", "user"})
    public Response<Boolean> auditRequirement(Long requirementId, Integer auditRes, BaseUser user);

    /**
     * 通过需求团队人员对于一个需求进行阶段变迁处理（前提这个需求必须是已经通过审核的了）
     * @param requirementId 需求编号
     * @param user          操作人员
     * @return  Boolean
     * 返回操作结果
     */
    @Export(paramNames = {"requirementId", "user"})
    public Response<Boolean> transitionStatus(Long requirementId, BaseUser user);

    /**
     * 查询一个需求下的所有可参与讨论的用户(包括供应商&采购商)
     * @param requirementId 需求编号
     * @return  List
     * 返回讨论组人员信息
     */
    @Export(paramNames = {"requirementId"})
    public Response<List<TopicUser>> findTopicPeople(Long requirementId);

    /**
     * 获取全部需求数量（不包括删除的需求数量）
     * @return  Long
     * 返回统计数据
     */
    @Export
    public Response<Long> findRequirementCount();

    /**
     * 系统自动的状态转换操作（定时操作）
     */
    public void transitionExpire();

    /**
     * 当阶段处于->承诺底线时，提前WARNING_DAY天提醒当提交方案的供应商数量少于3个的情况下会发送消息给采购商
     * @param warningDay 提前预警天数
     */
    public void warningExpire(int warningDay);

    /**
     * 定时自动的统计模块数据到需求统计数据
     */
    public void dumpCountModuleToRedis();

    /**
     * 通过需求Id查询需求的模块类型
     * @param requirementId 需求ID
     * @return 模块类型
     */
    @Export(paramNames = "requirementId")
    public Response<Long> findModuleTypeByRequirementId(Long requirementId);
}
