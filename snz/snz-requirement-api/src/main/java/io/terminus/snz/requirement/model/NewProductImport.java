package io.terminus.snz.requirement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.format.DateTimeFormat;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>新品导入信息</P>
 * <p>
 *     使用数据映射的方式导入数据，避免以后数据变更带来的重构
 * </p>
 * Created by wanggen 2014-08-14 10:04:08
 * <PRE>
 * id                       Long        自增主键
 * supplierCode             String      供应商代码
 * supplierName             String      供应商名称
 * documentName             String      文件
 * moduleId                 Long        模块ID
 * moduleNum                String      模块编号 海尔使用
 * originalCreationDate     Date        原件创建时间
 * prototypeSendDate        Date        原件发布时间
 * purchaseConfirmer        String      采购确认人
 * purchaseConfirmDate      Date        采购确认时间
 * sampleFinishDate         Date        样机完成时间
 * assemblyFinishDate       Date        装配完成时间
 * assemblyConclusion       String      装配结论
 * outTester                String      外检员
 * sampleReceiveDate        Date        收样确认时间
 * testSampleReceiveTime    Date        检测收样时间
 * testSampleReceiver       String      检测收样人
 * testStartDate            Date        检测开始时间
 * testPlanedDate           Date        检测计划时间
 * testEndDate              Date        检测完成时间
 * testConclusion           String      检测结论
 * finalConclusion          String      最终结论
 * createdAt                Date        创建时间
 * updatedAt                Date        修改时间
 * </PRE>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class NewProductImport implements Serializable {

// LASTUPDATE,              未确认字段
// HRPROTOTYPESENDQUANT,
// HRSMALLPRODDATE,
// HRTESTMANAGER,

    private static final long serialVersionUID = 3148438358402010814L;

    private Long id;                               //自增主键

    private String supplierCode;                   //供应商代码

    @Column(name = "SUPPLIERNAME")
    private String supplierName;                   //供应商名称

    @Column(name = "DOCUMENTNAME")
    private String documentName;                   //文件

    private Long moduleId;                         //模块ID，导入数据时根据模块编号 partNumber 确定

    @Column(name = "PARTNUMBER")
    private String moduleNum;                      //模块编号，海尔使用

    @Column(name = "ORIGINALCREATIONDATE", typeHandler = "date", datePattern = "YYYY/MM/dd-HH:mm:ss:SSS")
    private Date originalCreationDate;             //原件创建时间

    @Column(name = "HRPROTOTYPESENDDATE", typeHandler = "date", datePattern = "YYYY/MM/dd")
    private Date prototypeSendDate;                //原件发布时间

    @Column(name = "HRPURCHASECONFIRMER")
    private String purchaseConfirmer;              //采购确认人

    @Column(name = "HRPURCHASECONFIRMDATE", typeHandler = "date", datePattern = "YYYY/MM/dd")
    private Date purchaseConfirmDate;              //采购确认时间

    @Column(name = "HRSAMPLEFINISHDATE", typeHandler = "date", datePattern = "YYYY/MM/dd")
    private Date sampleFinishDate;                 //样机完成时间

    @Column(name = "HRASSEMBLYFINISHDATE", typeHandler = "date", datePattern = "YYYY/MM/dd-HH:mm:ss:SSS")
    private Date assemblyFinishDate;               //装配完成时间

    @Column(name = "HRASSEMBLYCONCLUSION")
    private String assemblyConclusion;             //装配结论

    @Column(name = "HROUTTESTER")
    private String outTester;                      //外检员

    @Column(name = "HRSAMPLERECEIVEDATE", typeHandler = "date", datePattern = "YYYY/MM/dd-HH:mm:ss:SSS")
    private Date sampleReceiveDate;                //收样确认时间

    @Column(name = "HRTESTSAMPLERECEIVETIME", typeHandler = "date", datePattern = "YYYY/MM/dd-HH:mm:ss:SSS")
    private Date testSampleReceiveTime;            //检测收样时间

    @Column(name = "HRTESTSAMPLERECEIVER")
    private String testSampleReceiver;             //检测收样人

    @Column(name = "HRTESTSTARTDATE", typeHandler = "date", datePattern = "YYYY/MM/dd")
    private Date testStartDate;                    //检测开始时间

    @Column(name = "HRTESTPLANEDDATE", typeHandler = "date", datePattern = "YYYY/MM/dd")
    private Date testPlanedDate;                   //检测计划时间

    @Column(name = "HRTESTENDDATE", typeHandler = "date", datePattern = "YYYY/MM/dd")
    private Date testEndDate;                      //检测完成时间

    @Column(name = "HRTESTCONCLUSION")
    private String testConclusion;                 //检测结论

    @Column(name = "HRFINALCONCLUSION")
    private String finalConclusion;                //最终结论

    private Date createdAt;                        //创建时间

    private Date updatedAt;                        //修改时间


    @Target(value = ElementType.FIELD)
    @Retention(value = RetentionPolicy.RUNTIME)
    public static @interface Column {
        String name();
        String typeHandler() default "string";
        String datePattern() default "";
    }


    public static interface TypeHandler{
        Object resolve(Object val, Object...ext);
    }

    public static Map<String, TypeHandler> handlers = new HashMap<String, TypeHandler>(){
        {
            put("date", new TypeHandler() {
                public Object resolve(Object val, Object... ext) {
                    try{
                        if(val==null || val.toString().trim().equals(""))
                            return null;
                        if(ext.length>=1)
                            return DateTimeFormat.forPattern((String) ext[0]).parseDateTime((String) val).toDate();
                    }catch (Exception e){
                        log.error("Invalid date value:[{}] and pattern:[{}]", val, ext[0]);
                    }
                    return null;
                }
            });
            put("string", new TypeHandler() {
                @Override
                public Object resolve(Object val, Object... ext) {
                    return (String)val;
                }
            });
        }
    };


    /**
     * 新品数据目前有8个步骤，检测当前最大日期是不为空，若不为空，则将该日期相应的步骤作为
     * 该模块进行到的步骤
     *
     * @return 该新品当前处于哪个流程步骤
     */
    public NewProductStep.Step detectStep() {
        if (this.getTestEndDate() != null) {                    // 8. 测试完成
            return NewProductStep.Step.testEnd;
        } else if (this.getTestPlanedDate() != null) {          // 7. 测试计划
            return NewProductStep.Step.testPlaned;
        } else if (this.getTestStartDate() != null) {           // 6. 测试开始
            return NewProductStep.Step.testStart;
        } else if (this.getTestSampleReceiveTime() != null) {   // 5. 测试收样
            return NewProductStep.Step.testSampleReceive;
        } else if (this.getSampleReceiveDate() != null) {       // 4. 样品接收
            return NewProductStep.Step.sampleReceive;
        } else if (this.getAssemblyFinishDate() != null) {      // 3. 装配完成
            return NewProductStep.Step.assemblyFinish;
        } else if (this.getPrototypeSendDate() != null) {       // 2. 原件发布
            return NewProductStep.Step.prototypeSend;
        } else if (this.getOriginalCreationDate() != null) {    // 1. 原件创建
            return NewProductStep.Step.originalCreation;
        }
        return NewProductStep.Step.originalCreation;
    }

}
