package io.terminus.snz.requirement.ignore;

import io.terminus.common.utils.JsonMapper;
import io.terminus.snz.requirement.dao.ModuleDao;
import org.apache.commons.dbcp.BasicDataSource;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.*;

/**
 * Desc:测试访问海尔的Oracle数据库
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-02.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/mysql-dao-context-test.xml"})
public class OracleTest {
    private static JsonMapper JSON_MAPPER = JsonMapper.JSON_NON_EMPTY_MAPPER;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ModuleDao moduleDao;

    private Connection connection;

    private PreparedStatement preparedStatement;

    private ResultSet resultSet;

    @Test
    public void testOracle() throws SQLException {
        try {
            BasicDataSource basicDataSource = (BasicDataSource) context.getBean("eaiSource");
            connection = basicDataSource.getConnection();

            preparedStatement = connection.prepareStatement("select * from pdmm501b.viewForhrNotif");
            resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();

            int i = 0;
            while (i++ < metaData.getColumnCount()) {
                System.out.println("columnName->" + metaData.getColumnName(i) + "------TypeName->" + metaData.getColumnTypeName(i));
            }
        }catch (Exception e){

        }finally {
            this.close();
        }
    }

    @Test
    public void testHrModExa() throws SQLException {
        try {
            BasicDataSource basicDataSource = (BasicDataSource) context.getBean("plmSource");
            connection = basicDataSource.getConnection();

            preparedStatement = connection.prepareStatement("select * from admm501b.hrmodexa");
            resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();

            int i = 0;
            while (i++ < metaData.getColumnCount()) {
                System.out.println(metaData.getColumnName(i) + "------TypeName->" + metaData.getColumnTypeName(i));
            }

            while (resultSet.next()) {
                for (int n = 1; n < 20; n++) {
                    System.out.println(metaData.getColumnName(n) + ":" + resultSet.getString(n));
                }
                System.out.println();
            }
        }catch (Exception e){

        }finally {
            this.close();
        }

        moduleDao.findById(1l);
    }

    @Test
    public void testDB(){
        try{
            System.out.println("do haisdjis");
            String sql = "insert into admm501b.hrmodexa(PartNumber , hrDemandID, hrDemandName, hrDemandRelPerson, hrDemandRelTime, hrModuleExampleID, hrModuleExampleName) values(?,?,?,?,?,?,?)";
            BasicDataSource basicDataSource = (BasicDataSource)context.getBean("plmSource");
            connection = basicDataSource.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            for(int i=0; i<4; i++){
                preparedStatement.setString(1 , i+1+"");
                preparedStatement.setString(2 , i+6+"");
                preparedStatement.setString(3 , "name");
                preparedStatement.setString(4 , "Person");
                preparedStatement.setString(5 , "RelTime");
                preparedStatement.setString(6 , "ExampleID");
                preparedStatement.setString(7 , "ExampleName");

                //批量插入操作
                preparedStatement.addBatch();
            }

            System.out.println(preparedStatement.executeBatch().length);
        }catch(Exception e){

        }finally {
            this.close();
        }
    }

    public static void main(String[] args){
        System.out.println(Days.daysBetween(new DateTime("2014-08-13") , DateTime.now()).getDays());
    }

    /**
     * 关闭数据库连接数据源对象
     */
    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (preparedStatement != null) {
                preparedStatement.close();
            }

            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
        }
    }
}
