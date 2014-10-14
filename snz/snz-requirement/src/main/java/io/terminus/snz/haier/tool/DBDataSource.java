package io.terminus.snz.haier.tool;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Desc:与海尔对接直接走数据库（在这边设置数据库连接对象）
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-07.
 */
@Slf4j
@Component
public class DBDataSource {
    public static final String EAI_SOURCE = "eaiSource";

    public static final String PLM_SOURCE = "plmSource";

    private final ApplicationContext context;

    @Autowired
    public DBDataSource(ApplicationContext context){
        this.context = context;
    }

    /**
     * 通过资源名称获取数据库访问db对象
     * @param sourceName 数据库资源名称
     * @return BasicDataSource
     * 返回资源对象
     */
    public BasicDataSource getSource(String sourceName){
        return (BasicDataSource)context.getBean(sourceName);
    }
}
