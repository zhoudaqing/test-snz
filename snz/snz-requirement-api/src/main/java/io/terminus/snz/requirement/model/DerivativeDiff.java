package io.terminus.snz.requirement.model;

import com.google.common.base.Objects;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by jiaoyuan on 14-7-7.
 */
@ToString
@EqualsAndHashCode
public class DerivativeDiff implements Serializable{
    private static final long serialVersionUID = -7525074827800655542L;
    @Getter
    @Setter
    private  Long id;       //自增主键
    @Getter
    @Setter
    private  Long requirementId;        //需求id
    @Getter
    @Setter
    private  Long moduleId;             //模块id
    @Getter
    @Setter
    private  String moduleName;         //模块名字
    @Getter
    @Setter
    private  Integer bomModule;         //模块BOM(模块主关件不变，0:子件增加,1:子件减少,2:子件变更)
    @Getter
    @Setter
    private  Integer matrix;            //模具(模块主关件符合衍生号要求，0:子件增加,1:子件减少,2:子件变更)
    @Getter
    @Setter
    private  Integer material;          //材料（0:相同,1:不同）
    @Getter
    @Setter
    private  Integer surfaceTreatment;  //表面处理工艺（0：喷涂，1：电镀，2：喷粉，3：其他）
    @Getter
    @Setter
    private  Integer printing;          //印刷（0：颜色，1：商标，2：文字，3：花纹）
    @Getter
    @Setter
    private  Integer softwareParam;     //软件、参数（0：软件，1：参数）
    @Getter
    @Setter
    private  Integer structure;         //结构（0：同截面不同长度，1：加工细节）
    @Getter
    @Setter
    private  Integer overseasParts;     //是否用于海外散件（0：否，1：是）
    @Getter
    @Setter
    private  Integer hostChange;        //主关件参数变化（0：电压频率，1：其他）
    @Getter
    @Setter
    private  Integer drive;             //接口相同，传动方式或传动比不同（传动方式：0，传动比：1）
    @Getter
    @Setter
    private  String description;        //差异其他描述
    @Getter
    @Setter
    private Date createdAt;             //创建时间
    @Getter
    @Setter
    private Date updatedAt;             //修改时间

    public enum BomModule{
        INCREASE(0 , "子件增加"), DECREASE(1, "子件减少"), CHANGE(2, "子件变更");

        private final Integer value;

        private final String description;

        private BomModule(Integer value , String description){
            this.value = value;

            this.description = description;
        }

        public static BomModule from(Integer value){
            for(BomModule bomModule: BomModule.values()){
                if(Objects.equal(value, bomModule.value)){
                    return bomModule;
                }
            }

            return null;
        }

        public Integer value(){
            return value;
        }

        @Override
        public String toString(){
            return description;
        }
    }

    public enum Material{
        SAME(0, "相同"), DIFFERENT(1, "不同");

        private final Integer value;

        private final String description;

        private Material(Integer value , String description){
            this.value = value;

            this.description = description;
        }

        public static Material from(Integer value){
            for(Material material: Material.values()){
                if(Objects.equal(value, material.value)){
                    return material;
                }
            }

            return null;
        }

        public Integer value(){
            return value;
        }

        @Override
        public String toString(){
            return description;
        }
    }

    public enum SurfaceTreatment{
        PAINT(0, "喷涂"), ELECTROPLATE(1, "电镀"),DUST(2,"喷粉"),OTHER(3,"其他");

        private final Integer value;

        private final String description;

        private SurfaceTreatment(Integer value , String description){
            this.value = value;

            this.description = description;
        }

        public static SurfaceTreatment from(Integer value){
            for(SurfaceTreatment surfaceTreatment: SurfaceTreatment.values()){
                if(Objects.equal(value, surfaceTreatment.value)){
                    return surfaceTreatment;
                }
            }

            return null;
        }

        public Integer value(){
            return value;
        }

        @Override
        public String toString(){
            return description;
        }
    }

    public enum Printing{
        COLOR(0, "颜色"), BRAND(1, "商标"),TEXT(2,"文字"),OTHER(3,"花纹");

        private final Integer value;

        private final String description;

        private Printing(Integer value , String description){
            this.value = value;

            this.description = description;
        }

        public static Printing from(Integer value){
            for(Printing printing: Printing.values()){
                if(Objects.equal(value, printing.value)){
                    return printing;
                }
            }

            return null;
        }

        public Integer value(){
            return value;
        }

        @Override
        public String toString(){
            return description;
        }
    }
}
