package io.terminus.snz.eai.util;

import com.thoughtworks.xstream.XStream;
import io.terminus.snz.eai.dto.InfoSelectedFromMDM;
import io.terminus.snz.eai.dto.MDMConfigureRow;
import io.terminus.snz.eai.dto.MDMModuleInfoRow;

/**
 * Date: 7/30/14
 * Time: 13:24
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public class MDMResultParser {

    private static final XStream xs;

    private static MDMResultParser parser;

    static {
        xs = new XStream();
        xs.autodetectAnnotations(true);
        xs.processAnnotations(InfoSelectedFromMDM.class);
        xs.processAnnotations(MDMConfigureRow.class);
        xs.processAnnotations(MDMModuleInfoRow.class);
        xs.ignoreUnknownElements();
    }

    public static MDMResultParser getInstance() {
        if (parser==null) {
            parser = new MDMResultParser();
        }
        return parser;
    }

    /**
     * 将一个xml使用xsteam字符串转换为java对象
     *
     * @param data     Java 字符串
     * @param klass    Java 对象，InfoSelectedFromMDM 或者老品信息
     * @return 一个
     */
    public <T> InfoSelectedFromMDM<T> parse(String data, Class<T> klass) {
        return (InfoSelectedFromMDM<T>) xs.fromXML(data);
    }
}
