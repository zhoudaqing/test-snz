package io.terminus.snz.requirement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * 新品导入步骤信息<P>
 *
 * Created by wanggen 2014-08-14 10:15:59
 * <PRE>
 * id                       Long        自增主键
 * parentId                 Long        父ID
 * moduleNum                String      模块编号
 * supplierCode             String      供应商代码
 * supplierName             String      供应商名称
 * step                     Integer     流程节点(1:原件创建 | 2:原件发布 | 3:装配完成 | 4:收样确认 | 5:检测收样 | 6:检测开始 | 7:检测计划时间 | 8:检测完成)
 * datetime                 Date        计划时间或日期
 * duration                 Integer     周期
 * realDatetime             Date        时间进度时间
 * status                   Integer     状态(-1:拖期 | 0:未进行 | 1:正常)
 * inCharge                 String      责任人
 * createdAt                Date        创建时间
 * updatedAt                Date
 * </PRE>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewProductStep implements Serializable {

    private static final long serialVersionUID = -604035251541671665L;

    private Long id;                               //自增主键

    private Long parentId;                         //父ID

    private String moduleNum;                      //模块号

    private String supplierCode;                   //供应商代码

    private String supplierName;                   //供应商名称

    private Integer step;                          //流程节点(1:原件创建 | 2:原件发布 | 3:装配完成 | 4:收样确认 | 5:检测收样 | 6:检测开始 | 7:检测计划时间 | 8:检测完成)

    private Date datetime;                         //计划时间或日期

    private Integer duration;                      //周期

    private Date realDatetime;                     //时间进度时间

    private Integer status;                        //状态(-1:拖期 | 0:未进行 | 1:正常)

    private String inCharge;                       //责任人

    private Date createdAt;                        //创建时间

    private Date updatedAt;                        //


    /**
     * 1. 根据新品数据信息、新品步骤信息合并一个新品步骤
     *      1.1 设置 parentId、moduleNum、supplierCode、supplierName
     *      1.2 设置步骤 step
     *      1.3 设置实际完成时间  realDatetime
     *      1.4 预订时间与时间时间设置状态
     *      1.5 设置责任人
     * @param prod 输入的新品数据
     * @param inStep 该新品所属步骤信息
     * @return 数据合并的新步骤
     */
    public static NewProductStep createFrom(NewProductImport prod, NewProductStep inStep){
        checkNotNull(prod);
        NewProductStep productStep = inStep==null ? new NewProductStep() : inStep;
        Step step = prod.detectStep();

        if(inStep!=null)
            checkState(step.value() == inStep.getStep(), "新品数据的步骤必须与已有的步骤同步");
        
        Date realDatetime = null;
        Integer status;

        productStep.setParentId(prod.getId());
        productStep.setModuleNum(prod.getModuleNum());
        productStep.setSupplierCode(prod.getSupplierCode());
        productStep.setSupplierName(prod.getSupplierName());
        productStep.setStep(step.value());


        switch (step){
            case originalCreation:{
                realDatetime = prod.getOriginalCreationDate();
                break;
            }
            case prototypeSend:{
                realDatetime = prod.getPrototypeSendDate();
                break;
            }
            case assemblyFinish:{
                realDatetime = prod.getAssemblyFinishDate();
                break;
            }
            case sampleReceive:{
                realDatetime = prod.getSampleReceiveDate();
                break;
            }
            case testSampleReceive:{
                realDatetime = prod.getTestSampleReceiveTime();
                productStep.setInCharge(prod.getTestSampleReceiver());
                break;
            }
            case testStart:{
                realDatetime = prod.getTestStartDate();
                break;
            }
            case testPlaned:{
                realDatetime = prod.getTestPlanedDate();
                break;
            }
            case testEnd:{
                realDatetime = prod.getTestEndDate();
                break;
            }
        }
        productStep.setRealDatetime(realDatetime);
        if(productStep.getDatetime()!=null){
            int compVal = new DateTime(realDatetime).withTimeAtStartOfDay().toDate().
                    compareTo(new DateTime(productStep.getDatetime()).withTimeAtStartOfDay().toDate());
            status = compVal > 0 ? -1 : 1;
            productStep.setStatus(status);
        }else
            productStep.setStatus(1); //没有计划日期，认为当前步骤正常
        return productStep;
    }


    /**
     * 1:原件创建 | 2:原件创建 | 3:装配完成 | 4:收样确认 | 5:检测收样 | 6:检测开始 | 7:检测计划时间 | 8:检测完成
     */
    public static enum Step{
        originalCreation(1, "原件创建"),
        prototypeSend(2, "原件发布"),
        assemblyFinish(3, "装配完成"),
        sampleReceive(4, "收样确认"),
        testSampleReceive(5, "检测收样"),
        testStart(6,"检测开始"),
        testPlaned(7, "检测计划时间"),
        testEnd(8, "检测完成");

        public static Step[] steps = {null,
                originalCreation,  prototypeSend, assemblyFinish, sampleReceive,
                testSampleReceive, testStart, testPlaned, testEnd
        };
        private final int value;
        private final String description;
        private Step(int value, String description) {
            this.value = value;
            this.description = description;
        }
        public static Step fromValue(int value){
            if(value<=0 || value>=9)
                throw new IllegalArgumentException("Step int value:"+value+" cant not be greater than 8 and lower than 1");
            return steps[value];
        }
        public int value() {
            return this.value;
        }
        @Override
        public String toString() {
            return description;
        }
    }


    /** -1:拖期 | 0:未进行 | 1:正常 **/
    public static enum Status {
        delayed(-1, "拖期"),
        normal(0, "未进行"),
        in_advance(1, "正常");
    
        private final int value;
        private final String description;
        private Status(int value, String description) {
            this.value = value;
            this.description = description;
        }
        public int value() {
            return this.value;
        }
        @Override
        public String toString() {
            return description;
        }
    }

}
