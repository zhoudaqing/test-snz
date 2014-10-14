package io.terminus.snz.requirement.ignore;

import io.terminus.snz.requirement.dao.BasicTest;
import io.terminus.snz.requirement.model.Module;
import io.terminus.snz.requirement.tool.AnalyzeExcel;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

/**
 * Desc:
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-08.
 */
@Ignore
public class AnalyzeExcelTest extends BasicTest{

    /*
     * 模拟数据6000多条数据－》解析时间3499ms
     */
    public static String fileURL="http://qd.baidupcs.com/file/fb0e61880bf061a0ca50da1a6812b635?fid=68346277-250528-391672583014715&time=1399605008&sign=FDTAXER-DCb740ccc5511e5e8fedcff06b081203-DHei9SPjCG6bkF9449KTVOQcJ%2FE%3D&to=qb&fm=Q,B,U,nc&newver=1&expires=8h&rt=pr&r=336269267&logid=3248593104&vuk=68346277&fn=%E4%BA%8C%E7%BA%A7%E5%9F%9F%E5%90%8D%E8%A7%84%E5%88%92%20%284%29.xlsx";

    @Test
    public void Test() throws Exception{

        AnalyzeExcel analyzeExcel = new AnalyzeExcel();
        List<Module> moduleList = analyzeExcel.analyzeURL(fileURL, 0, 1, new AnalyzeExcel.AnalyzeAction<Module>() {
            @Override
            public Module transform(String[] info) {
                Module module = new Module();
                try{

                    for (int i = 0; i < info.length; i++) {
                        switch (i){
                            case 0: {
                                module.setRequirementId(new Float(info[i]).longValue());
                                break;
                            }
                            case 1: {
                                module.setModuleName(info[i]);
                                break;
                            }
                        }
                    }
                }catch(NumberFormatException e){
                    e.printStackTrace();
                }

                return module;
            }
        });

        //System.out.println(moduleList.get(0).getSpuName());
    }
}
