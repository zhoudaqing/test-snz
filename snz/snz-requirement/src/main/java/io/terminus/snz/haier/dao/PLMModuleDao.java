package io.terminus.snz.haier.dao;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.snz.haier.model.PLMModule;
import io.terminus.snz.haier.tool.DBDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp.BasicDataSource;
import org.elasticsearch.common.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Desc:海尔PLM系统数据访问对象
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-07.
 */
@Slf4j
@Component
public class PLMModuleDao {
    private static Date date = new Date();

    private static int addNum = 0;

    //999999个数据足够满足在1s内的同步数据唯一
    private static final int MAX_NUM = 999999;

    private final BasicDataSource dataSource;

    private Connection connection;

    private PreparedStatement preparedStatement;

    private ResultSet resultSet;

    @Autowired
    public PLMModuleDao(DBDataSource dbDataSource){
        this.dataSource = dbDataSource.getSource(DBDataSource.PLM_SOURCE);
    }

    /**
     * 实现创建模块数据信息在PLM中间表
     * @param module    模块数据信息
     * @return  Boolean
     * 返回创建是否成功
     */
    public Boolean create(PLMModule module){
        Boolean result = false;

        try{
            String sql = "insert into admm501b.hrmodexa(CLASS, CREATIONDATE, CURDBNAME, OBID, SUPPLIERID, SUPPLIERNAME, hrBuNo, hrDemandID, "
                    +"hrDemandName, hrDemandRelPerson, hrDemandRelTime, hrModuleExampleID, hrModuleExampleName) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            //需要加入海尔那边的oracle数据库的固定字段信息
            preparedStatement.setString(1 , "hrModExa");
            preparedStatement.setString(2 , DateTime.now().toLocalDateTime().toString());
            preparedStatement.setString(3 , "admm501b");
            preparedStatement.setString(4 , uniqueCode());
            preparedStatement.setString(5 , module.getSupplierVId());
            preparedStatement.setString(6 , module.getSupplierName());
            preparedStatement.setString(7 , module.getBuNo());
            preparedStatement.setString(8 , module.getDemandId());
            preparedStatement.setString(9 , module.getDemandName());
            preparedStatement.setString(10 , module.getDemandRelPerson());
            preparedStatement.setString(11 , module.getDemandRelTime());
            preparedStatement.setString(12 , module.getModuleExampleId());
            preparedStatement.setString(13 , module.getModuleExampleName());

            preparedStatement.executeUpdate();
            result = true;
        }catch(Exception e){
            log.error("create PLM module failed, PLMModule=({}), error code={}", module, Throwables.getStackTraceAsString(e));
        }finally {
            this.close();
        }

        return result;
    }

    /**
     * 根据模块的数据列表批量创建模块信息在PLM系统上
     * @param moduleList 模块列表
     * @return  Integer
     * 返回创建的数据条数
     */
    public Integer createBatch(List<PLMModule> moduleList){
        Integer number = 0;

        try{
            String sql = "insert into admm501b.hrmodexa(CLASS, CREATIONDATE, CURDBNAME, OBID, SUPPLIERID, SUPPLIERNAME, hrBuNo, hrDemandID, "
                        +"hrDemandName, hrDemandRelPerson, hrDemandRelTime, hrModuleExampleID, hrModuleExampleName) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            for(PLMModule module : moduleList){
                //需要加入海尔那边的oracle数据库的固定字段信息
                preparedStatement.setString(1 , "hrModExa");
                preparedStatement.setString(2 , DateTime.now().toLocalDateTime().toString());
                preparedStatement.setString(3 , "admm501b");
                preparedStatement.setString(4 , uniqueCode());
                preparedStatement.setString(5 , module.getSupplierVId());
                preparedStatement.setString(6 , module.getSupplierName());
                preparedStatement.setString(7 , module.getBuNo());
                preparedStatement.setString(8 , module.getDemandId());
                preparedStatement.setString(9 , module.getDemandName());
                preparedStatement.setString(10 , module.getDemandRelPerson());
                preparedStatement.setString(11 , module.getDemandRelTime());
                preparedStatement.setString(12 , module.getModuleExampleId());
                preparedStatement.setString(13 , module.getModuleExampleName());

                preparedStatement.addBatch();
            }

            number = preparedStatement.executeBatch().length;
        }catch(Exception e){
            log.error("create batch PLM module failed, error code={}", Throwables.getStackTraceAsString(e));
        }finally {
            this.close();
        }

        return number;
    }

    /**
     * 更新plm中间表的对应模块的
     * @param moduleList
     * @return Integer
     * 返回更改了几条供应商V码数据
     */
    public Integer updateSupplierVId(List<PLMModule> moduleList){
        Integer number = 0;

        try{
            String sql = "update admm501b.hrmodexa set SUPPLIERID=?, SUPPLIERNAME=? where hrModuleExampleID=?";
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            //批量更新数据
            for(PLMModule module : moduleList){
                preparedStatement.setString(1 , module.getSupplierVId());
                preparedStatement.setString(2 , module.getSupplierName());
                preparedStatement.setString(3 , module.getModuleExampleId());

                preparedStatement.addBatch();
            }

            number = preparedStatement.executeBatch().length;
        }catch(Exception e){
            log.error("update batch PLM module failed, error code={}", Throwables.getStackTraceAsString(e));
        }finally {
            this.close();
        }

        return number;
    }

    /**
     * 根据模块编号列表查询PLM中间表的数据信息
     * @param moduleIds 需要查询的模块编号列表
     * @return  List
     * 返回plm系统生成的模块专用号
     */
    public Map<Long , String> findAllModule(List<String> moduleIds){
        Map<Long , String> moduleMap = Maps.newHashMap();

        try{
            String sql = "select hrDemandID, PartNumber from admm501b.hrmodexa where PartNumber is not null and hrDemandID in (";

            StringBuilder builder = new StringBuilder();
            for(int i=0; i<moduleIds.size(); i++){
                builder.append((i == moduleIds.size() - 1) ? "?)" : "?,");
            }
            sql += builder.toString();

            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for(int i=0; i<moduleIds.size(); i++){
                preparedStatement.setString(i+1 , moduleIds.get(i));
            }

            preparedStatement.executeQuery();
            resultSet = preparedStatement.getResultSet();
            while(resultSet.next()){
                moduleMap.put(Long.parseLong(resultSet.getString(1)) , resultSet.getString(2));
            }
        }catch(Exception e){
            log.error("find all module failed, error code={}", Throwables.getStackTraceAsString(e));
        }finally {
            this.close();
        }

        return moduleMap;
    }

    /**
     * 根据moduleId获取所有的PLM中间表数据
     * @param moduleId 模块编号
     * @return  List
     * 返回中间表数据
     */
    public List<PLMModule> findByModuleId(String moduleId){
        List<PLMModule> moduleList = Lists.newArrayList();

        try{
            String sql = "select SUPPLIERID, SUPPLIERNAME, hrBuNo, hrDemandID, hrDemandName, hrDemandRelPerson, hrDemandRelTime, hrModuleExampleID, hrModuleExampleName from admm501b.hrmodexa where hrDemandID=?";
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, moduleId);

            preparedStatement.executeQuery();
            resultSet = preparedStatement.getResultSet();

            PLMModule plmModule;
            while(resultSet.next()){
                plmModule = new PLMModule();
                plmModule.setSupplierVId(resultSet.getString(1));
                plmModule.setSupplierName(resultSet.getString(2));
                plmModule.setBuNo(resultSet.getString(3));
                plmModule.setDemandId(resultSet.getString(4));
                plmModule.setDemandName(resultSet.getString(5));
                plmModule.setDemandRelPerson(resultSet.getString(6));
                plmModule.setDemandRelTime(resultSet.getString(7));
                plmModule.setModuleExampleId(resultSet.getString(8));
                plmModule.setModuleExampleName(resultSet.getString(9));

                moduleList.add(plmModule);
            }
        }catch(Exception e){
            log.error("find PLM module failed, moduleId={}, error code={}", moduleId, Throwables.getStackTraceAsString(e));
        }finally {
            this.close();
        }

        return moduleList;
    }

    /**
     * 关闭数据库连接数据源对象
     */
    private void close(){
        try{
            if(resultSet != null){
                resultSet.close();
            }

            if(preparedStatement != null){
                preparedStatement.close();
            }

            if(connection != null){
                connection.close();
            }
        }catch (Exception e){
            log.error("close db source failed, error code={}", Throwables.getStackTraceAsString(e));
        }
    }

    /**
     * 获取唯一的需求系统流水号(使用同步保证在并发情况下的数据的唯一性)
     * @return String
     * 返回一个唯一的系统流水编号
     */
    private static synchronized String uniqueCode(){
        //获取增量数据
        addNum = addNum > MAX_NUM ? 0 : addNum;

        date.setTime(System.currentTimeMillis());

        return String.format("%1$tY%1$tm%1$td%1$tk%1$tM%1$tS%2$06d", date, addNum++);
    }
}
