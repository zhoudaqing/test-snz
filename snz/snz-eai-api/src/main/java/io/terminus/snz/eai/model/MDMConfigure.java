package io.terminus.snz.eai.model;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 与MDM对接后，MDM 不定时跟新到 SNZ 平台的配置信息，主要用来申请供应商V码
 *
 * Date: 7/25/14
 * Time: 9:58
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public class MDMConfigure implements Serializable {

    private static final long serialVersionUID = 8218338946551456324L;

    @Getter
    @Setter
    private Long id;

    @Getter
    private String code;

    @Getter
    @Setter
    private String name;

    @Getter
    private Integer type;

    public void setCode(String code) {
        this.code = code.replace(" ", "");
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setTypeEnum(TYPE type) {
        this.type = type==null?null:type.toValue();
    }

    public static enum TYPE {
        TERM(1, "付款条款", "PayTerm"),
        SLAVE(2, "统奴科目", "ReconcileAccount"),
        NATION(3, "银行国家", "Country"),
        METHOD(4, "付款方式", "PayMethod"),
        CODE(5, "排序码", "OrderCode"),
        FINGROUP(6, "账户组", "Fin"),
        COMPANYCODE(7, "公司代码", "CompanyCode"),
        PURCHASERGROUP(8, "采购组织", "PurchaseOrganization");


        private int value;
        private String display;
        private String valueId;

        private TYPE(int v, String d, String valueId) {
            this.value = v;
            this.display = d;
            this.valueId = valueId;
        }

        public Integer toValue() {
            return this.value;
        }

        public String toDisplay() {
            return this.display;
        }


        public static TYPE fromValueID(String vi) {
            if (Strings.isNullOrEmpty(vi)) return null;

            for (TYPE t:TYPE.values()) {
                if (Objects.equal(t.valueId, vi))
                    return t;
            }
            return null;
        }

        public static Integer valueIdToType(String vi) {
            TYPE t = fromValueID(vi);
            return t==null?null:t.value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }
}
