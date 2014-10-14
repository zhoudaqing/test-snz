package io.terminus.snz.sms.haier;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.util.Map;

/**
 * Author:  <a href="mailto:jlchen.cn@gmail.com">jlchen</a>
 * Date: 2013-04-02
 */
public class XmlTranslator {
    private final static XStream xstream = new XStream();

    static {
        xstream.autodetectAnnotations(true);
        xstream.setMode(XStream.NO_REFERENCES);
    }

    @SuppressWarnings("unchecked")
    private static <T> T fromXML(String xml) {
        return (T) xstream.fromXML(xml);
    }

    /**
     * 反序列化 XML 到 pojo
     *
     * @param xml             XML 文本
     * @param preProcessClass 反序列化映射的POJO
     * @param <T>             映射泛型
     * @return 反序列化后的实体
     */
    public static <T> T fromXML(String xml, Class preProcessClass) {
        xstream.processAnnotations(preProcessClass);
        return fromXML(xml);
    }

    /**
     * 将实例 POJO 转换为 XML 文本
     *
     * @param t   实例对象
     * @param <T> 泛型参数
     * @return XML 文本
     */
    public static <T> String toXML(T t) {
        return xstream.toXML(t);
    }


    @SuppressWarnings("unused")
    public static class MapEntryConverter implements Converter {
        public boolean canConvert(Class clazz) {
            return Map.class.isAssignableFrom(clazz);
        }

        @SuppressWarnings("unchecked")
        public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
            Map<String, String> map = (Map<String, String>) value;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                writer.startNode(entry.getKey());
                writer.setValue(entry.getValue());
                writer.endNode();
            }
        }

        public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
            ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();

            while (reader.hasMoreChildren()) {
                reader.moveDown();
                builder.put(reader.getNodeName(), reader.getValue());
                reader.moveUp();
            }
            return builder.build();
        }
    }
}
