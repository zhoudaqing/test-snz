package io.terminus.snz.web.controllers.file;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Files;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

/**
 * Author:  <a href="mailto:i@terminus.io">jlchen</a>
 * Date: 2014-09-16
 */
public class NginxFileServer implements FileServer {
    private final static String UPLOAD_FILE = "file";

    private final static Logger log = LoggerFactory.getLogger(NginxFileServer.class);

    private final DateTimeFormatter dtf = DateTimeFormat.forPattern("/yyyy/MM/dd/");

    private final String serverUrl;

    public NginxFileServer(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    @Override
    public String writeImage(String fileName, MultipartFile file) throws FileUploadException {
        //todo 后期可能对图片做压缩等处理
        String targetUrl = "http://" + serverUrl + "/upload/images";

        return write(targetUrl , fileName, file, FileType.FILE_IMAGE);
    }

    @Override
    public String writeDoc(String fileName, MultipartFile file) throws FileUploadException {
        String targetUrl = "http://" + serverUrl + "/upload/docs";

        return write(targetUrl , fileName, file, FileType.FILE_DOC);
    }

    @Override
    public boolean delete(String fileName) throws FileDeleteException {
        return false;
    }

    /**
     * 向远程服务器写入文件
     * @param url       远程服务器路径
     * @param fileName  文件名称
     * @param file      文件数据
     * @param fileType  文件类型
     * @return  String
     * 返回文件相对保存路径
     * @throws FileUploadException
     */
    private String write(String url , String fileName, MultipartFile file, FileType fileType) throws FileUploadException {
        InputStream inputStream = null;

        HttpRequest request = HttpRequest.post(url);

        try {
            inputStream = file.getInputStream();
            request.part(UPLOAD_FILE , fileName, null, inputStream);

            if (request.ok()) {
                String realPath;

                switch (fileType){
                    case FILE_IMAGE:
                        realPath = "http://" + serverUrl + "/images" + dtf.print(DateTime.now()) + fileName;
                        break;

                    case FILE_DOC:
                        realPath = "http://" + serverUrl + "/docs" + dtf.print(DateTime.now()) + fileName;
                        break;

                    default:
                        realPath = null;
                }

                return realPath;
            } else {
                log.error("failed to upload file({}) to file server,http response code:{}, response body:{}", fileName, request.code(), request.body());
                throw new FileUploadException(request.code() + "");
            }
        } catch (Exception e) {
            log.error("upload to nginx file server failed, exception:", e);
            throw new FileUploadException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    //ignore this error
                }
            }
        }
    }
}
