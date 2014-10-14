package io.terminus.snz.requirement.service;

import com.google.common.base.Objects;
import io.terminus.common.model.Paging;
import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dto.DetailQuotaDto;
import io.terminus.snz.requirement.dto.SolutionQuotaDto;
import io.terminus.snz.requirement.dto.SolutionRankDto;
import io.terminus.snz.requirement.model.ModuleQuota;
import io.terminus.snz.requirement.model.RequirementRank;
import io.terminus.snz.requirement.model.RequirementSolution;

import java.util.List;

/**
 * Desc:(在选定供应商与方案阶段)整体需求&模块需求配额处理
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-07.
 */
public interface RequirementQuotaService {
    public static enum EndStatus {
        BUS_NEG(1, "商务谈判状态"),
        SUP_SIGN(2, "供应商跟标"),
        RES_PUB(3, "配额结果公示");

        private final int value;

        private final String display;

        private EndStatus(int value, String display) {
            this.value = value;
            this.display = display;
        }

        public static EndStatus from(int value) {
            for(EndStatus endStatus : EndStatus.values()) {
                if(Objects.equal(endStatus.value, value)) {
                    return endStatus;
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

    public static enum QueryType {
        QUALIFY(1, "供应商资质符合"),
        ALL(2, "全部条件符合");

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
     * 通过需求编号&排序方式确认最终的需求方案名次信息
     * @param requirementId 需求编号
     * @param sortType  方案的排序方式(1:技术,2:质量,3:互动,4:产能,5:成本。当前先按照这两种方式进行排序，后序逻辑再添加。default:5)
     * @param user      只有需求创建者才能看见配额分配的tab(其它的返回一个空的数据)
     * @return  List
     * 返回需求方案的名次信息
     */
    @Export(paramNames = {"requirementId" , "sortType", "user"})
    public Response<List<RequirementSolution>> findEndSolutions(Long requirementId , Integer sortType, BaseUser user);

    /**
     * 通过需求编号以及供应商用户信息查询能够进入最后阶段的方案数量
     * @param requirementId 需求编号
     * @param queryType     查询处理（1:查询供应商资质符合的人数，2:查询全部条件符合的人数）
     * @return  Integer
     * 返回最终的方案数量
     */
    @Export(paramNames = {"requirementId" , "queryType"})
    public Response<Integer> findEndSolutionNum(Long requirementId , Integer queryType);

    /**
     * 查询某个需求下可以入围的供应商方案列表
     * @param requirementId 需求编号
     * @return  List
     * 入围的需求方案信息
     */
    public Response<List<RequirementSolution>> findTagSolutions(Long requirementId);

    /**
     * 批量创建方案排名信息（同时会生成模块的配额分配）
     * @param requirementId  需求编号
     * @param sortType  排序类型（1:技术,5:成本。这个对于模块配额的分配方式有关系）
     * @param user      只有需求创建者才能看见配额分配的tab(其它的返回一个空的数据)
     * @return  Boolean
     * 返回创建结果（这个创建完成就无法再更改）
     */
    @Export(paramNames = {"requirementId", "sortType", "user"})
    public Response<Boolean> createRanks(Long requirementId , Integer sortType, BaseUser user);

    /**
     * 通过需求编号&用户信息查询需求的方案以及排名信息
     * @param requirementId 需求编号
     * @param user          操作人员信息
     * @return  List
     * 返回需求方案以及排名信息列表
     */
    @Export(paramNames = {"requirementId" , "user"})
    public Response<List<SolutionRankDto>> findTSolutionRank(Long requirementId , BaseUser user);

    /**
     * 这个是针对按照新的规则来的T排名以及配额的分配：名次是由采购上自己选择的
     * @param requirementRanks  需求的方案排名
     * @param user              操作者
     * @return  Boolean
     * 放回创建结果
     */
    @Export(paramNames = {"requirementRanks" , "user"})
    public Response<Boolean> createRankByT(String requirementRanks , BaseUser user);

    /**
     * 选择需求最终状态的类型（1.商务谈判状态，2.供应商跟标，3.配额结果公示）
     * @param requirementId 需求编号
     * @param endStatus     最终状态
     * @param user          用户信息
     * @return  Boolean
     * 返回需求状态切换是否成功
     */
    @Export(paramNames = {"requirementId" , "endStatus", "user"})
    public Response<Boolean> selectEndStatus(Long requirementId, Integer endStatus, BaseUser user);

    /**
     * 通过模块的配额编号实现用户更标操作
     * @param quotaId   配额编号
     * @param status    状态
     * @param user      用户
     * @return  Boolean
     * 返回处理结果
     */
    @Export(paramNames = {"quotaId" , "status", "user"})
    public Response<Boolean> signSolution(Long quotaId , Integer status, BaseUser user);

    /**
     * 模块配额信息修改（只有需求创建人,因为整体的配额100%需要确定，所以只能是更改一个模块的配额）
     * @param moduleQuotas  模块配额数据列表
     * @param user          操作用户
     * @return  Boolean
     * 返回更改结果
     */
    @Export(paramNames = {"moduleQuotas" , "user"})
    public Response<Boolean> updateQuota(String moduleQuotas , BaseUser user);

    /**
     * 通过需求编号查询该需求下全部的针对于需求的方案信息（需求所有信息都填写完整的是指模块方案信息）
     * @param requirementId 需求编号
     * @return List
     * 返回实际的需求方案信息列表
     * （同时还需要注入供应商的实际TQRDC数据－》从海尔获取）
     */
    @Export(paramNames = {"requirementId"})
    public Response<List<SolutionQuotaDto>> findDetailSolution(Long requirementId);

    /**
     * 通过需求编号获取该需求下的所有排名信息
     * @param requirementId 需求编号
     * @param type          类型（1:正选供应商，2:备选供应商，3:显示全部数据－》默认3）
     * @return  List
     * 返回排名信息
     */
    @Export(paramNames ={"requirementId", "type"} )
    public Response<List<RequirementRank>> findRequirementRanks(Long requirementId , Integer type);

    /**
     * 通过需求编号查询方案下的整体模块的实际配额详情（分页处理）
     * @param requirementId 需求编号
     * @param pageNo        页面编号（默认0）
     * @param size          页数（默认20）
     * @return  Paging
     * 返回所有的模块配额数据信息
     */
    @Export(paramNames = {"requirementId", "pageNo", "size"})
    public Response<Paging<ModuleQuota>> findByRequirementId(Long requirementId, Integer pageNo, Integer size);

    /**
     * 通过登录的用户信息&需求编号获取供应商对应与某个需求的模块配额信息
     * @param user          用户对象
     * @param requirementId 需求编号
     * @param pageNo        页面编号（默认0）
     * @param size          页数（默认20）
     * @return Paging
     * 返回所有的模块配额数据信息
     */
    @Export(paramNames = {"user", "requirementId", "pageNo", "size"})
    public Response<Paging<ModuleQuota>> findQuotasBySupplier(BaseUser user, Long requirementId, Integer pageNo, Integer size);

    /**
     * 通过需求编号查询需求下的所有实际配额详情（这个用于前台需求详情页的tab显示，分页处理，只显示当前已跟标的供应商配额信息）
     * @param requirementId 需求编号
     * @param pageNo        页面编号（默认0）
     * @param size          页数（默认20）
     * @return Paging
     * 返回所有的模块配额数据信息
     */
    @Export(paramNames = {"requirementId", "pageNo", "size"})
    public Response<DetailQuotaDto> findRequirementQuota(Long requirementId , Integer pageNo, Integer size);

    /**
     * 根据模块编号&模块的工厂编号获取模块下的所有的数据
     * @param moduleId          模块编号
     * @param moduleFactoryId   模块的工厂
     * @return List
     * 返回所有的供应商的工厂的配额数据
     */
    @Export(paramNames = {"moduleId" , "moduleFactoryId"})
    public Response<List<ModuleQuota>> findModuleQuotas(Long moduleId, Long moduleFactoryId);

    /**
     * 定时写入供应商v码到plm中间表
     */
    public void plmCompanyVExpire();
}
