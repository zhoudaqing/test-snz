package io.terminus.snz.requirement.dao;

import io.terminus.snz.requirement.model.ImportGood;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static io.terminus.common.utils.Arguments.notNull;

/**
 * Date: 7/9/14
 * Time: 15:17
 * Author: 2014年 <a href="mailto:dong.worker@gmail.com">张成栋</a>
 */

@Repository
public class ImportGoodDao extends SqlSessionDaoSupport {

    /**
     * 插入一条新的新品导入详情记录
     *
     * @param importGood    新品导入详情记录
     * @return        新插入记录的ID
     */
    public Long create(ImportGood importGood) {
        getSqlSession().insert("ImportGood.create", importGood);
        return importGood.getId();
    }

    /**
     * 尝试升级一条原有的新品导入详情记录
     *
     * @param importGood    新品导入详情记录
     * @return              操作是否成功
     */
    public Boolean update(ImportGood importGood) {
        return getSqlSession().update("ImportGood.update", importGood) == 1;
    }

    /**
     * 根据ID查找一条新品导入记录详情
     *
     * @param id    新品导入详情的ID
     * @return      一条ID与给出ID相符的新品导入详情记录，未找到返回null
     */
    public ImportGood findById(Long id) {
       return getSqlSession().selectOne("ImportGood.findById", id);
    }

    /**
     * 根据给定对象的参数查找一条新品导入记录详情
     *
     * @param params    给定用来参照的对象，除了创建、更新时间，对象的其他非空属性作为查找的依据
     * @return          一条符合调教的记录，未找到返回null
     */
    public ImportGood findOneBy(ImportGood params) {
        checkArgument(notNull(params));

        return getSqlSession().selectOne("ImportGood.findOneBy", params);
    }

    /**
     * 根据给定对象的参数查找新品导入记录详情列表
     *
     * @param param    给定用来参照的对象，除了创建、更新时间，对象的其他非空属性作为查找的依据
     * @return         一个新品导入记录详情的列表
     */
    public List<ImportGood> findBy(ImportGood param) {
        checkArgument(notNull(param));

        return getSqlSession().selectList("ImportGood.findBy", param);
    }
}
