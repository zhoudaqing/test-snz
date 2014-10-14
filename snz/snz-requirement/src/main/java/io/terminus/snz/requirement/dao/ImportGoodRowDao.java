package io.terminus.snz.requirement.dao;

import io.terminus.snz.requirement.model.ImportGoodRow;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static io.terminus.common.utils.Arguments.isNullOrEmpty;
import static io.terminus.common.utils.Arguments.notNull;

/**
 * Date: 7/9/14
 * Time: 15:17
 * Author: 2014年 <a href="mailto:dong.worker@gmail.com">张成栋</a>
 */

@Repository
public class ImportGoodRowDao extends SqlSessionDaoSupport {

    /**
     * 创建一条新的新品导入详情行记录
     *
     * @param goodRow    一条新的新品导入详情行记录
     * @return           新纪录的ID
     */
    public Long create(ImportGoodRow goodRow) {
        checkArgument(notNull(goodRow));

        getSqlSession().insert("ImportGoodRow.create", goodRow);
        return goodRow.getId();
    }

    /**
     * 尝试更新一条已经存在的记录
     *
     * @param goodRow    更新到这条新的记录，除了ID至少有一个属性不为空
     * @return           操作是否成功
     */
    public Boolean update(ImportGoodRow goodRow) {
        checkArgument(notNull(goodRow));

        return getSqlSession().update("ImportGoodRow.update", goodRow) == 1;
    }

    /**
     * 根据给出的ID查找ID相同的对象
     *
     * @param id    给定的ID，作为查找条件
     * @return      一条ID和给定ID相同的记录
     */
    public ImportGoodRow findById(Long id) {
        return getSqlSession().selectOne("ImportGoodRow.findById", id);
    }

    /**
     * 根据给定的条件查找一条记录
     *
     * @param params    给定作为条件的对象，除了创建、更新日期，至少有一个属性不为空
     * @return          一条符合条件的记录
     */
    public ImportGoodRow findBy(ImportGoodRow params) {
        checkArgument(notNull(params));

        return getSqlSession().selectOne("ImportGoodRow.findBy", params);
    }

    public List<ImportGoodRow> findByIds(List<Long> rowIds) {
        checkArgument(!isNullOrEmpty(rowIds));

        return getSqlSession().selectList("ImportGoodRow.findByIds", rowIds);
    }

    public Boolean updateForce(ImportGoodRow foundRow) {
        return getSqlSession().update("ImportGoodRow.updateForce", foundRow) == 1;
    }
}
