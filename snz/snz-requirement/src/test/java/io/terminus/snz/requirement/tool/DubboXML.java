package io.terminus.snz.requirement.tool;

import com.google.common.base.Objects;
import io.terminus.pampas.client.Export;
import jodd.io.FileUtil;
import jodd.jerry.Jerry;
import jodd.jerry.JerryFunction;
import org.elasticsearch.common.Strings;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Desc:
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-08-27.
 */
public class DubboXML {
    //服务版本
    private static final String VERSION = "1.0";

    //服务协议
    private static final String PROTOCOL = "dubbo";

    //认证类型
    private static final String AUTH_TYPE = "1";

    //服务标签
    private static final String TAGS = "690,hr";

    private static Map<String , String> xmlPath = new HashMap<String, String>(){
        {
            this.put("category.xml" , "/Users/MichaelZhao/ZeroSpace/snz/snz-category/src/main/resources/spring/category-dubbo-provider.xml");
            this.put("eai.xml" , "/Users/MichaelZhao/ZeroSpace/snz/snz-eai/src/main/resources/spring/eai-dubbo-provider.xml");
            this.put("message.xml" , "/Users/MichaelZhao/ZeroSpace/snz/snz-message/src/main/resources/spring/message-dubbo-provider.xml");
            this.put("related.xml" , "/Users/MichaelZhao/ZeroSpace/snz/snz-related/src/main/resources/spring/related-dubbo-provider.xml");
            this.put("sns.xml" , "/Users/MichaelZhao/ZeroSpace/snz/snz-sns/src/main/resources/spring/sns-dubbo-provider.xml");
            this.put("requirement.xml" , "/Users/MichaelZhao/ZeroSpace/snz/snz-requirement/src/main/resources/spring/requirement-dubbo-provider.xml");
            this.put("statistic.xml" , "/Users/MichaelZhao/ZeroSpace/snz/snz-statistic/src/main/resources/spring/statistic-dubbo-provider.xml");
            this.put("user.xml" , "/Users/MichaelZhao/ZeroSpace/snz/snz-user/src/main/resources/spring/user-dubbo-provider.xml");
        }

        private static final long serialVersionUID = 7067941892886107459L;
    };

    private Element root = null;

    public static void main(String[] args){
        DubboXML dubboXMl = new DubboXML();
        for(Map.Entry<String , String> entry : xmlPath.entrySet()){
            try {
                dubboXMl.createDubboXml("/Users/MichaelZhao/ZeroSpace/xmlDubbo/"+entry.getKey() , entry.getValue());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createDubboXml(String savePath , String analysePath) throws IOException {
        //xml文档
        //File file = new File(DubboXML.class.getResource(analysePath).getFile());
        File file = new File(analysePath);
        //解析xml元素
        Jerry doc = Jerry.jerry(FileUtil.readString(file));

        Jerry dubboInterface = doc.$("beans").children();

        try {
            root = new Element("services");
            root.setAttribute("almId" , "690");
            root.setAttribute("packageCode", "requirement");
        } catch (Exception e) {
            e.printStackTrace();
        }

        dubboInterface.each(new JerryFunction() {
            @Override
            public boolean onNode(Jerry $this, int index) {
                String className = $this.attr("interface");

                if(className != null){
                    try {
                        //写入xml信息
                        generateXml(root, className);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                return true;
            }
        });

        Format format = Format.getCompactFormat();
        format.setEncoding("UTF-8");
        format.setIndent("    "); //缩进4个空格后换行
        XMLOutputter XMLOut = new XMLOutputter(format);
        XMLOut.output(root, new FileOutputStream(savePath));
    }


    /**
     * 生成XML文件
     * @return
     */
    public static void generateXml(Element root, String interfaceName) throws ClassNotFoundException {
        //配置在dubbo中的接口信息
        Class obj = Class.forName(interfaceName);

        Method[] methods = obj.getMethods();

        //服务信息
        Element service = new Element("service");
        service.setAttribute("interface" , interfaceName);
        service.setAttribute("version" , VERSION);
        service.setAttribute("protocol" , PROTOCOL);
        service.setAttribute("authType" , AUTH_TYPE);
        service.setAttribute("tags" , TAGS);
        root.addContent(service);

        for(Method serviceMethod : methods){
            //获取返回类型
            Type returnType = findEndType(serviceMethod.getGenericReturnType());

            //描述Dto数据信息
            if(returnType != null){
                //获取返回对象的className
                String className = Strings.replace(returnType.toString(), "class ", "");
                Class returnObj = Class.forName(className);

                //表示当前服务包中用到的数据结构信息
                Element dataStructure = new Element("dataStructure");
                dataStructure.setAttribute("type" , returnType.toString());
                root.addContent(dataStructure);

                //数据结构<dataStructure>的子元素,用来表示数据中的一个属性字段，
                //如EmployeeDTO这个数据结构中包含name,age属性，那么name,age称为两个field
                Field[] filedList = returnObj.getDeclaredFields();
                Element field;
                for(Field fieldInfo : filedList){
                    if(Objects.equal(fieldInfo.getName() , "serialVersionUID")){
                        continue;
                    }
                    field = new Element("field");
                    field.setAttribute("name" , fieldInfo.getName());
                    field.setAttribute("type" , fieldInfo.getType().toString());
                    dataStructure.addContent(field);
                }
            }

            //服务的方法
            Element method = new Element("method");
            method.setAttribute("name" , serviceMethod.getName());

            Class[] paramTypes = serviceMethod.getParameterTypes();

            Annotation[] annotations = serviceMethod.getAnnotations();

            System.out.println("annotations:"+annotations.length);

            //方法参数信息
            Element args = new Element("args");
            method.addContent(args);

            Export export = null;
            if(annotations != null && annotations.length > 0){
                export = (Export)annotations[0];
            }
            for(int i=0; i<paramTypes.length; i++){
                Element arg = new Element("arg");
                arg.setAttribute("name" , export == null ? " " : export.paramNames()[i]);
                arg.setAttribute("type", paramTypes[i].getName());
                arg.setAttribute("index" , i+"");
                arg.setAttribute("required" , "true");
                args.addContent(arg);
            }

            service.addContent(method);
        }
    }

    //获取类型的最终范型数据
    private static Type findEndType(Type type){
        //判断是否示范型
        if(type.toString().contains("Dto")){
            //判断是否示范型
            if (type instanceof ParameterizedType){
                //泛型类型列表
                Type[] types = ((ParameterizedType)type).getActualTypeArguments();
                for (Type newType : types) {
                    return findEndType(newType);
                }
                return null;
            }else{
                return type;
            }
        }else{
            return null;
        }
    }
}
