package io.terminus.snz.related.daos;

import io.terminus.snz.related.BaseDaoTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

/**
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-7-12
 */
public class FactoryOrganDaoTest extends BaseDaoTest {
    @Autowired
    private FactoryOrganDao factoryOrganDao;

    @Test
    public void testImport() throws IOException {
//        String filepath = "/Users/haolin/Working/docs/690/sap/工厂与组织对照关系.XLS";
//        FileInputStream excel = new FileInputStream(new File(filepath));
//        Workbook workbook = new HSSFWorkbook(excel);
//        Sheet sheet = workbook.getSheetAt(0);
//        int rows = sheet.getLastRowNum() + 1;
//        System.out.println("total rows: " + rows);
//        int start = 1;
//        List<FactoryOrgan> factoryOrgans = new ArrayList<FactoryOrgan>();
//        for (int i = start; i < rows; i++){
//            Row row = sheet.getRow(i);
//            String factory = row.getCell(1).toString().substring(0, 4);
//            String organ = row.getCell(2).toString().substring(0, 4);
//            System.out.print(i+ ": " + factory + "\t" + organ);
//            System.out.println();
//            if (!Objects.equal("9000", organ)){
//                factoryOrgans.add(new FactoryOrgan(factory, organ));
//            }
//        }
//        System.out.println(factoryOrgans.size());
//        factoryOrganDao.creates(factoryOrgans);
    }

    @Test
    public void testFindByFactory(){
        List<String> organs = factoryOrganDao.findOrganByFactory("9010");
        System.out.println(organs.size());
//        for (String organ: organs){
//            System.out.println(organ);
//        }
    }
}
