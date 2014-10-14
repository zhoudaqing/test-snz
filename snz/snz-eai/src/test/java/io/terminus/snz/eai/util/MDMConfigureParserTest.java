package io.terminus.snz.eai.util;

import io.terminus.snz.eai.dto.InfoSelectedFromMDM;
import io.terminus.snz.eai.dto.MDMConfigureRow;
import io.terminus.snz.eai.dto.MDMModuleInfoRow;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Date: 8/6/14
 * Time: 10:12
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public class MDMConfigureParserTest {

    private String configData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<OUTPUT>\n" +
            "    <ROWSET>\n" +
            "        <row>\n" +
            "            <RN>1.0</RN>\n" +
            "            <VALUE_ID>2008021800000000</VALUE_ID>\n" +
            "            <VALUE_SET_ID>CompanyCode</VALUE_SET_ID>\n" +
            "            <DESCRIPTION>公司代码</DESCRIPTION>\n" +
            "            <VALUE>6120</VALUE>\n" +
            "            <VALUE_MEANING>海尔集团电器产业有限公司</VALUE_MEANING>\n" +
            "            <VALUE_MEANING_EN>海尔集团电器产业有限公司</VALUE_MEANING_EN>\n" +
            "            <ACTIVE_FLAG>1</ACTIVE_FLAG>\n" +
            "            <CREATED>2008-02-28T18:43:00</CREATED>\n" +
            "            <LAST_UPD>2012-07-10T10:08:10</LAST_UPD>\n" +
            "            <DELETE_FLAG>0</DELETE_FLAG>\n" +
            "        </row>\n" +
            "        <row>\n" +
            "            <RN>2.0</RN>\n" +
            "            <VALUE_ID>2008021800000003</VALUE_ID>\n" +
            "            <VALUE_SET_ID>CompanyCode</VALUE_SET_ID>\n" +
            "            <DESCRIPTION>公司代码</DESCRIPTION>\n" +
            "            <VALUE>6740</VALUE>\n" +
            "            <VALUE_MEANING>海尔集团电器产业有限公司</VALUE_MEANING>\n" +
            "            <VALUE_MEANING_EN>海尔集团电器产业有限公司</VALUE_MEANING_EN>\n" +
            "            <ACTIVE_FLAG>0</ACTIVE_FLAG>\n" +
            "            <CREATED>2008-02-28T18:43:00</CREATED>\n" +
            "            <LAST_UPD>2011-06-10T14:00:11</LAST_UPD>\n" +
            "            <DELETE_FLAG>1</DELETE_FLAG>\n" +
            "        </row>\n" +
            "   </ROWSET>\n" +
            "</OUTPUT>\n";

    private String moduleInfoData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<OUTPUT>\n" +
            "    <ROWSET>\n" +
            "        <row>\n" +
            "            <RN>1.0</RN>\n" +
            "            <ROW_ID>2007123010157536</ROW_ID>\n" +
            "            <MATERIAL_CODE>0060201169</MATERIAL_CODE>\n" +
            "            <MATERIAL_DESCRITION>回气管保温管可发PP</MATERIAL_DESCRITION>\n" +
            "            <MATERIAL_TYPE>ROH</MATERIAL_TYPE>\n" +
            "            <PRIMARY_UOM>EA</PRIMARY_UOM>\n" +
            "            <MTL_GROUP_CODE>2S0057</MTL_GROUP_CODE>\n" +
            "            <DEPARTMENT>00</DEPARTMENT>\n" +
            "            <FAMILY_NAME>HPFM1006</FAMILY_NAME>\n" +
            "            <FAMILY_RELCLASS>冰共体管件</FAMILY_RELCLASS>\n" +
            "            <LAST_UPD>2012-07-31T17:42:11</LAST_UPD>\n" +
            "            <DELETE_FLAG>0</DELETE_FLAG>\n" +
            "            <HR_DOUBLE_DISTRIIND>N</HR_DOUBLE_DISTRIIND>\n" +
            "        </row>\n" +
            "        <row>\n" +
            "            <RN>2.0</RN>\n" +
            "            <ROW_ID>2007123010157538</ROW_ID>\n" +
            "            <MATERIAL_CODE>0010300506</MATERIAL_CODE>\n" +
            "            <MATERIAL_DESCRITION>ROHS-壳体PCM板(定尺料1738.6*1000.2*0.7)</MATERIAL_DESCRITION>\n" +
            "            <MATERIAL_TYPE>ROH</MATERIAL_TYPE>\n" +
            "            <PRIMARY_UOM>EA</PRIMARY_UOM>\n" +
            "            <MTL_GROUP_CODE>2S0120</MTL_GROUP_CODE>\n" +
            "            <DEPARTMENT>00</DEPARTMENT>\n" +
            "            <FAMILY_NAME>HPFM0860</FAMILY_NAME>\n" +
            "            <FAMILY_RELCLASS>其它卷板</FAMILY_RELCLASS>\n" +
            "            <LAST_UPD>2011-04-25T14:43:15</LAST_UPD>\n" +
            "            <DELETE_FLAG>0</DELETE_FLAG>\n" +
            "            <PRODUCT_ALLOCATION>008</PRODUCT_ALLOCATION>\n" +
            "            <HR_DOUBLE_DISTRIIND>N</HR_DOUBLE_DISTRIIND>\n" +
            "        </row>" +
            "   </ROWSET>\n" +
            "</OUTPUT>\n";

    @Test
    public void shouldParseConfigure () {
        MDMResultParser parser = MDMResultParser.getInstance();
        InfoSelectedFromMDM<MDMConfigureRow> rowset =
                parser.parse(configData, MDMConfigureRow.class);
        assertNotNull(rowset);
        assertNotNull(rowset.getROWSET());
        assertEquals(2, rowset.getROWSET().size());
    }

    @Test
    public void shouldParseModuleInfo() {
        InfoSelectedFromMDM<MDMModuleInfoRow> result =
                MDMResultParser.getInstance().parse(moduleInfoData, MDMModuleInfoRow.class);
        assertNotNull(result);
        assertFalse(result.getROWSET().isEmpty());
        assertEquals(2, result.getROWSET().size());
        assertNull(result.getROWSET().get(0).getHRMODULARNAME());
    }
}
