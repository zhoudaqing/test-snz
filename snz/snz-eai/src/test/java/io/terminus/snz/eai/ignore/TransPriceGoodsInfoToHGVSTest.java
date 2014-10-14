package io.terminus.snz.eai.ignore;

import org.example.transpricegoodsinfotohgvs.OutType;
import org.example.transpricegoodsinfotohgvs.TransPriceGoodsInfoToHGVS;
import org.example.transpricegoodsinfotohgvs.TransPriceGoodsInfoToHGVSService;
import org.example.transpricegoodsinfotohgvs.ZDWHJHPT;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 发送配额信息到EAI测试：
 * 数据类型及对应验证规则:
 定义	      数据类型	         验证规则
 新品	      050910	   01020304050615293738
 老品开新点	   0509	       010203040615293738
 调价	        07	       01030407152425273738
 维护配额	        10	              010203
 */
@Ignore
public class TransPriceGoodsInfoToHGVSTest {

    @Test
	public void testTransPriceGoodsInfoToHGVS(){
		TransPriceGoodsInfoToHGVSService serviceSrc = new TransPriceGoodsInfoToHGVSService();
		TransPriceGoodsInfoToHGVS service = serviceSrc.getTransPriceGoodsInfoToHGVSSOAP();
		List<ZDWHJHPT> datas = new ArrayList<ZDWHJHPT>();
		ZDWHJHPT data1 = new ZDWHJHPT();
		ZDWHJHPT data2 = new ZDWHJHPT();
		ZDWHJHPT data3 = new ZDWHJHPT();
		
		data1.setIDNO("201407220004");			//数据ID: 可按规则生成: 如日期+id
		data1.setMATNR("0030204413");			//物料号
		data1.setWERKS("9250");					//工厂
		data1.setLIFNR("V12861");				//供应商账号
		data1.setEKORG("9131");					//采购组织
		data1.setDATAT("050910"); 				//数据类型:新品
		data1.setCHECKR("01020304050615293738");//验证规则
		data1.setKPEIN(new BigDecimal(1));		//采购价格单位
		data1.setKMEIN("EA");					//采购单位
		data1.setZOA1(new BigDecimal(0));		//关税
		data1.setAGENTFEE(new BigDecimal(1.25));//代理费	
		data1.setWAERS("CNY");					//采购货币
		data1.setEKGRP("OE3");					//采购组
		data1.setMEINS("EA");					//基本计量单位
		data1.setTAX("JB");						//税码
		data1.setNETPR(new BigDecimal("7"));	//净价
		data1.setQUOTE(new BigDecimal(100));	//配额
		data1.setAPLFZ(new BigDecimal(2));		//采购期
		data1.setZAF1(new BigDecimal(0));		//其他费用
		data1.setTYPE("K");						//采购类型
		datas.add(data1);
		
		data2.setIDNO("201407220005");			//数据ID
		data2.setMATNR("0030204413");			//物料号
		data2.setWERKS("9220");					//工厂
		data2.setLIFNR("V13007");				//供应商账号
		data2.setEKORG("9111");					//采购组织
		data2.setDATAT("0509"); 				//数据类型:老品开新点	
		data2.setCHECKR("010203040615293738");	//验证规则
		data2.setKPEIN(new BigDecimal(1)); 		//采购价格单位
		data2.setKMEIN("EA");					//采购单位
		data2.setZOA1(new BigDecimal(0));		//关税
		data2.setAGENTFEE(new BigDecimal(1.25));//代理费	
		data2.setWAERS("CNY");					//采购货币
		data2.setEKGRP("OE3");					//采购组
		data2.setMEINS("EA");					//基本计量单位
		data2.setTAX("JB");						//税码
		data2.setNETPR(new BigDecimal("6"));	//净价
		data2.setQUOTE(new BigDecimal(70));		//配额
		data2.setAPLFZ(new BigDecimal(2));		//采购期
		data2.setZAF1(new BigDecimal(0));		//其他费用
		data2.setTYPE("K");						//采购类型
		datas.add(data2);
		
		data3.setIDNO("201407220006");
		data3.setMATNR("0030204413");
		data3.setWERKS("9220");
		data3.setLIFNR("V13182");
		data3.setEKORG("9111");	
		data3.setDATAT("07");
		data3.setCHECKR("01030407152425273738");
		data3.setKPEIN(new BigDecimal(1));
		data3.setKMEIN("EA");
		data3.setZOA1(new BigDecimal(0));
		data3.setAGENTFEE(new BigDecimal(1.25));
		data3.setWAERS("CNY");
		data3.setEKGRP("OE3");
		data3.setMEINS("EA");
		data3.setTAX("JB");
		data3.setNETPR(new BigDecimal("7.5"));
		data3.setQUOTE(new BigDecimal(30));		
		data3.setAPLFZ(new BigDecimal(2));		
		data3.setZAF1(new BigDecimal(0));		
		data3.setTYPE("K");						
		datas.add(data3);
		
		OutType result = service.transPriceGoodsInfoToHGVS(datas);
		
		System.out.println("undo: " + result.getUNDO());
		System.out.println("message: " + result.getMessage());
		System.out.println("flag: " + result.getFlag());
		System.out.println("fault detail: " + result.getFaultDetail());
		System.out.println("total: " + result.getTOTAL());
		System.out.println("total bak: " + result.getTOTALBAK());
	}

	/**
	 * 调价测试
	 */
	@Test
	public void testTransPriceGoodsInfoToHGVSByTiaoJia(){
		TransPriceGoodsInfoToHGVSService serviceSrc = new TransPriceGoodsInfoToHGVSService();
		TransPriceGoodsInfoToHGVS service = serviceSrc.getTransPriceGoodsInfoToHGVSSOAP();
		List<ZDWHJHPT> datas = new ArrayList<ZDWHJHPT>();
		ZDWHJHPT data1 = new ZDWHJHPT();
		ZDWHJHPT data2 = new ZDWHJHPT();
		
		data1.setIDNO("201407230005");			//数据ID
		data1.setMATNR("0030204413");			//物料号
		data1.setWERKS("9220");					//工厂
		data1.setLIFNR("V19864");				//供应商账号
		data1.setEKORG("9111");					//采购组织
		data1.setDATAT("07"); 					//数据类型:调价
		data1.setCHECKR("01030407152425273738");//验证规则
		data1.setKPEIN(new BigDecimal(1));		//采购价格单位
		data1.setKMEIN("EA");					//采购单位
		data1.setZOA1(new BigDecimal(0));		//关税
		data1.setAGENTFEE(new BigDecimal("2.6"));//代理费
		data1.setWAERS("CNY");					//采购货币
		data1.setEKGRP("OE3");					//采购组
		data1.setMEINS("EA");					//基本计量单位
		data1.setTAX("JB");						//税码
		data1.setNETPR(new BigDecimal("6"));	//净价
		data1.setQUOTE(new BigDecimal(70));		//配额
		data1.setAPLFZ(new BigDecimal(2));		//采购期
		data1.setZAF1(new BigDecimal(0));		//其他费用
		data1.setTYPE("K");						//采购类型
		datas.add(data1);
		
		data2.setIDNO("201407230006");			//数据ID
		data2.setMATNR("0030204413");			//物料号
		data2.setWERKS("9220");					//工厂
		data2.setLIFNR("V13044");				//供应商账号
		data2.setEKORG("9111");					//采购组织
		data2.setDATAT("07"); 					//数据类型:调价
		data2.setCHECKR("01030407152425273738");//验证规则
		data2.setKPEIN(new BigDecimal(1)); 		//采购价格单位
		data2.setKMEIN("EA");					//采购单位
		data2.setZOA1(new BigDecimal(0));		//关税
		data2.setAGENTFEE(new BigDecimal("2.6"));//代理费
		data2.setWAERS("CNY");					//采购货币
		data2.setEKGRP("OE3");					//采购组
		data2.setMEINS("EA");					//基本计量单位
		data2.setTAX("JB");						//税码
		data2.setNETPR(new BigDecimal("7.5"));	//净价
		data2.setQUOTE(new BigDecimal(30));		//配额
		data2.setAPLFZ(new BigDecimal(2));		//采购期
		data2.setZAF1(new BigDecimal(0));		//其他费用
		data2.setTYPE("K");						//采购类型
		datas.add(data2);
		
		OutType result = service.transPriceGoodsInfoToHGVS(datas);

		System.out.println("undo: " + result.getUNDO());
		System.out.println("message: " + result.getMessage());
		System.out.println("flag: " + result.getFlag());
		System.out.println("fault detail: " + result.getFaultDetail());
		System.out.println("total: " + result.getTOTAL());
		System.out.println("total bak: " + result.getTOTALBAK());
	}
}