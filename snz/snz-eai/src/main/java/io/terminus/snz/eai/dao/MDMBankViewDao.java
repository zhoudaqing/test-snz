package io.terminus.snz.eai.dao;

import io.terminus.common.model.Paging;
import io.terminus.snz.eai.model.MDMBankView;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Date: 8/5/14
 * Time: 11:39
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */

@Repository
public class MDMBankViewDao extends SqlSessionDaoSupport {
    public Long create(MDMBankView insert) {
        getSqlSession().insert("MDMBankView.create", insert);
        return insert.getId();
    }

    public Boolean update(MDMBankView update) {
        return getSqlSession().update("MDMBankView.update", update) == 1;
    }

    public MDMBankView findById(Long id) {
        return getSqlSession().selectOne("MDMBankView.findById", id);
    }

    public MDMBankView findBy(MDMBankView params) {
        return getSqlSession().selectOne("MDMBankView.findBy", params);
    }

    public Paging<MDMBankView> pagingBankFindByFuzzyName(Map<String,Object> params) {
        Paging<MDMBankView> paging;

        Long count = getSqlSession().selectOne("MDMBankView.findCountByFuzzyName",params);
        if(count==0){
            return new Paging<MDMBankView>(0l,new ArrayList<MDMBankView>());
        }
        List<MDMBankView> mdmBankViews = getSqlSession().selectList("MDMBankView.findByFuzzyName",params);

        paging = new Paging<MDMBankView>(count,mdmBankViews);
        return  paging;
    }

    public MDMBankView findBankByName(String name) {
        return getSqlSession().selectOne("MDMBankView.findByName", name);
    }
}
