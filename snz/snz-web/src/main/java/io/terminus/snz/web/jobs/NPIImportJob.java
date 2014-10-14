package io.terminus.snz.web.jobs;

import com.google.common.collect.Maps;
import io.terminus.common.annotation.RedisLock;
import io.terminus.snz.requirement.model.NewProductImport;
import io.terminus.snz.requirement.service.NewProductImportService;
import io.terminus.snz.web.helpers.FtpDownloader;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;

/**
 * <P>功能描述: NPI 导入任务</P>
 *
 * @author wanggen on 14-8-26.
 */
@Slf4j
@ToString
public class NPIImportJob {

    @Getter
    @Setter
    private String npiHost;         //ftp文件HOST

    @Getter
    @Setter
    private String npiPort;         //端口

    @Getter
    @Setter
    private String npiDir;          //文件夹路径

    @Getter
    @Setter
    private String npiUsername;     //用户名称

    @Getter
    @Setter
    private String npiPassword;     //密码

    @Getter
    @Setter
    private String fileEncoding;    //下载文件编码格式，默认 GBK


    @Autowired
    @Setter
    private NewProductImportService newProductImportService;

    public NPIImportJob() {}

    final DateTimeFormatter dateFmt = DateTimeFormat.forPattern("YYYYMMdd");


    /**
     * 远程下载 CSV 文件
     *
     * @throws java.io.IOException 若抛出网络异常，连接异常，终止该任务
     */
    @Scheduled(cron = "0 10 23 * * ?")
    @RedisLock(keyName = "NPIImportJob:importNPI", maxWait = 1000*60, expiredTime = 1000*60*60)
    public void importNPI() {

        FtpDownloader ftpDownloader = null;
        try {
            ftpDownloader = new FtpDownloader(npiHost, Integer.parseInt(npiPort), npiUsername, npiPassword);
        } catch (IOException e) {
            log.error("Failed to download from remote:[{}], Caused by:{}", this, e.toString(), e);
            throw new RuntimeException(e);
        }

        String dir = this.npiDir;
        String today = dateFmt.print(DateTime.now());
        final String fileName = today + "_VIEWFORHRNOTIF.CSV";

        final File destFile = new File(fileName);
        ftpDownloader.copyToLocal(dir, fileName);
        try {
            saveToDBFromFile(destFile);
        } catch (Exception e) {
            log.error("Error occurred when persisting to file:[{}]", destFile, e);
        }finally {
            destFile.delete();
        }
    }


    /**
     * 将下载后的文件存储到数据库<BR>
     * CSV 简单文件读取，只支持
     *
     * @param destFile 下载后的文件
     */
    public void saveToDBFromFile(File destFile) throws IOException, IllegalAccessException {

        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(destFile), fileEncoding));
            Map<String, Integer> header = Maps.newHashMap();
            String line;
            boolean headed = false;
            int i = 0;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.trim().equals("")){
                    continue;
                }
                String[] cells = line.split(",");
                if (!headed) {
                    for (int c = 0; c < cells.length; c++) {
                        if (cells[c].startsWith("#")){
                            cells[c] = cells[c].substring(1);
                        }
                        header.put(cells[c], c);
                    }
                    headed = true;
                    i++;
                    continue;
                }
                if (cells.length != header.size()) {
                    log.error("文件[{}]中第:[{}]行有非法数据:[{}], title长度:[{}], 数据长度:[{}]", destFile, i, Arrays.toString(cells), header.size(), cells.length);
                    cells = Arrays.copyOf(cells, header.size());
                }
                NewProductImport npiObj = getNewProductImport(header, cells);
                newProductImportService.create(npiObj);
                i++;
            }
        } finally {
            if(bufferedReader!=null){
                bufferedReader.close();
            }
            destFile.delete();
        }


    }


    /**
     * 通过类名映射填充 NewProductImport 实例对象
     *
     * @param header 列名对于数据的列位置
     * @param cells  数据行
     * @return 填充数据后的实例对象
     * @throws IllegalAccessException
     */
    private NewProductImport getNewProductImport(Map<String, Integer> header, String[] cells) throws IllegalAccessException {
        NewProductImport npiObj = new NewProductImport();
        Field[] fields = NewProductImport.class.getDeclaredFields();
        for (Field field : fields) {
            NewProductImport.Column column = field.getAnnotation(NewProductImport.Column.class);
            field.setAccessible(true);
            if (column != null) {
                String outerName = column.name();
                String val = cells[header.get(outerName)];
                NewProductImport.TypeHandler handler = NewProductImport.handlers.get(column.typeHandler());
                field.set(npiObj, handler.resolve(val, column.datePattern()));
            } else if (header.containsKey(field.getName())) {
                String val = cells[header.get(field.getName())];
                field.set(npiObj, val);
            }
        }
        return npiObj;
    }

}
