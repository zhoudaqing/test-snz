package io.terminus.snz.web.controllers.file;

import com.google.common.base.Objects;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * 图片服务API接口
 *
 * Author:  <a href="mailto:remindxiao@gmail.com">xiao</a>
 * Date: 2013-13-27
 */
public interface FileServer {
    public static enum FileType {
        FILE_IMAGE(1, "图片"),
        FILE_DOC(2, "文件");

        private final int value;

        private final String display;

        private FileType(int value, String display) {
            this.value = value;
            this.display = display;
        }

        public int value() {
            return value;
        }

        public String toString() {
            return display;
        }
    }

    /**
     * 上传图片信息
     * @param fileName  文件名
     * @param file      文件
     * @return  文件上传后的相对路径
     * @throws FileUploadException
     * @return String
     * 返回文件相对路径
     */
    public String writeImage(String fileName, MultipartFile file) throws FileUploadException;

    /**
     * 上传文件信息
     * @param fileName  文件名称
     * @param file      文件
     * @return 文件上传后的相对路径
     * @throws FileUploadException
     * @return String
     * 返回文件相对路径
     */
    public String writeDoc(String fileName, MultipartFile file) throws FileUploadException;

    /**
     * 刪除文件
     *
     * @param fileName  文件名
     * @return 是否刪除成功
     */
    public boolean delete(String fileName) throws FileDeleteException;
}
