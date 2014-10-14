package io.terminus.snz.web.controllers.file;

import com.google.common.base.Throwables;
import com.google.common.io.Files;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * Author:  <a href="mailto:i@terminus.io">jlchen</a>
 * Date: 2014-07-01
 */
public class LocalDiskFileServer implements FileServer {

    private final DateTimeFormatter dtf = DateTimeFormat.forPattern("/yyyy/MM/dd/");

    private final static Logger log = LoggerFactory.getLogger(LocalDiskFileServer.class);

    private final String fileFolder;

    private final String fileBaseUrl;

    public LocalDiskFileServer(String fileFolder, String fileBaseUrl) {
        this.fileFolder = fileFolder;

        this.fileBaseUrl = fileBaseUrl;
    }

    @Override
    public String writeImage(String fileName, MultipartFile file) throws FileUploadException {
        return write(fileName , file);
    }

    @Override
    public String writeDoc(String fileName, MultipartFile file) throws FileUploadException {
        return write(fileName , file);
    }

    /**
     * @param fileName 文件名
     * @param file     文件
     * @return 文件上传后的相对路径
     */
    public String write(String fileName, MultipartFile file) throws FileUploadException {
        try {
            final String path = fileFolder + dtf.print(DateTime.now()) + fileName;
            File to = new File(path);
            Files.createParentDirs(to);
            Files.write(file.getBytes(),to);
            return fileBaseUrl + dtf.print(DateTime.now()) + fileName;
        } catch (Exception e) {
            log.error("failed to save file :{}, cause:{}", fileName, Throwables.getStackTraceAsString(e));
            throw new FileUploadException("upload file failed", e);
        }
    }

    /**
     * 刪除文件
     *
     * @param fileName 文件名
     * @return 是否刪除成功
     */
    @Override
    public boolean delete(String fileName) throws FileDeleteException {
        try {
            boolean success = new File(fileFolder + File.pathSeparator+fileName).delete();
            if(!success){
                log.error("failed to delete file({})", fileName);
            }
            return true;
        } catch (Exception e) {
            log.warn("error happened when deleteFile {} , error:{}", fileName, Throwables.getStackTraceAsString(e));
            return false;
        }
    }
}