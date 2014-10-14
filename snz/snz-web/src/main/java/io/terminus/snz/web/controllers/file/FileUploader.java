package io.terminus.snz.web.controllers.file;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Sets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import io.terminus.common.exception.JsonResponseException;
import io.terminus.common.utils.JsonMapper;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.UserUtil;
import io.terminus.pampas.engine.MessageSources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

/**
 * Author:  <a href="mailto:i@terminus.io">jlchen</a>
 * Date: 2014-06-13
 */
@Controller
@RequestMapping("/api/files")
public class FileUploader {
    private final static Logger log = LoggerFactory.getLogger(FileUploader.class);

    private final static HashFunction md5 = Hashing.md5();

    private final static Set<String> allowed_image_types = Sets.newHashSet("jpeg" , "jpg", "png", "gif", "bmp", "psd", "svg");

    private final static Set<String> allowed_doc_types = Sets.newHashSet("doc" , "docx", "xlsx", "xls", "ppt", "pptx", "txt", "pdf");

    @Autowired
    private MessageSources messageSources;

    @Autowired
    private FileServer fileServer;

    @Value("#{app.fileBaseUrl}")
    private String fileBaseUrl;

    @Value("#{app.fileSizeMax}")
    private long fileSizeMax;


    @RequestMapping(value = "/{fileName}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void deleteImage(@PathVariable("fileName") String fileName) {
        BaseUser user = UserUtil.getCurrentUser();
        if (user == null) {
            throw new JsonResponseException(401, messageSources.get("user.not.login"));
        }
        if (!fileName.startsWith(user.getId().toString())) {
            log.error("failed to delete file(name={}), cause: not the owner, current userId={}", fileName, user.getId());
            throw new JsonResponseException(401, messageSources.get("file.delete.noauth"));
        }
        try {
            fileServer.delete(fileName);
        } catch (Exception e) {
            log.warn("error happened when deleteFile {} on file server, error:{}", fileName, Throwables.getStackTraceAsString(e));
        }
    }

    @RequestMapping(value = "/{fileName}/check", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void checkAuthDeleteImage(@PathVariable("fileName") String fileName) {
        BaseUser user = UserUtil.getCurrentUser();
        if (user == null) {
            throw new JsonResponseException(401, messageSources.get("user.not.login"));
        }
        if (!fileName.startsWith(user.getId().toString())) {
            log.error("failed to delete file(name={}), cause: not the owner, current userId={}", fileName, user.getId());
            throw new JsonResponseException(401, messageSources.get("file.delete.noauth"));
        }
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String processUpload(MultipartFile file) {
        BaseUser user = UserUtil.getCurrentUser();
        if (user == null) {
            throw new JsonResponseException(401, messageSources.get("user.not.login"));
        }

        Long userId = user.getId();
        String ext = Files.getFileExtension(file.getOriginalFilename());
        String saveFileName = userId + "_" + md5.hashString(file.getOriginalFilename(), Charsets.UTF_8)+"."+ext;

        try {
            byte[] fileData = file.getBytes();

            //if size of the file is more than fileSizeMax,it will raise an 500 error
            if (fileData.length > fileSizeMax) {
                log.error("file(name={}) is too large", file.getOriginalFilename());
                throw new JsonResponseException(500, messageSources.get("file.too.large"));
            }

            String filePath;
            if(allowed_image_types.contains(ext)){
                filePath = fileServer.writeImage(saveFileName, file);
            }else {
                filePath = fileServer.writeDoc(saveFileName , file);
            }

            //若成功返回路径则代表上传成功
            boolean isSucceed = !Strings.isNullOrEmpty(filePath);
            if (!isSucceed) {
                log.error("write file(name={}) of user(id={}) to image server failed", file.getOriginalFilename(), userId);
                throw new JsonResponseException(500, messageSources.get("file.upload.fail"));
            }

            return JsonMapper.nonEmptyMapper().toJson(new UploadedFile(file.getOriginalFilename(),
                    Long.valueOf(file.getSize()).intValue(), filePath));

        } catch (Exception e) {
            log.error("failed to process upload file {},cause:{}", file.getOriginalFilename(), Throwables.getStackTraceAsString(e));
            if(e instanceof JsonResponseException)
                throw (JsonResponseException)e;
            else
                throw new JsonResponseException(500, messageSources.get("file.upload.fail"));
        }

    }
}
