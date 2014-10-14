/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.console.extend;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-20
 */
public interface ImageServer {
    /**
     *
     * @param fileName  文件名
     * @param file      文件
     * @return  文件上传后的相对路径
     */
    String write(String fileName, MultipartFile file) throws ImageUploadException;


    /**
     * 处理原始文件名, 并返回新的文件名
     * @param originalName  原始文件名
     * @param imageData  原始文件的字节数组
     * @return  新的文件名
     */
    String handleFileName(String originalName, byte[] imageData);


    /**
     * 刪除文件
     *
     * @param fileName  文件名
     * @return 是否刪除成功
     */
    boolean delete(String fileName) throws ImageDeleteException;

    public class ImageUploadException extends Exception {
        private static final long serialVersionUID = 6295717443044899527L;

        public ImageUploadException() {
        }

        public ImageUploadException(String message) {
            super(message);
        }

        public ImageUploadException(String message, Throwable cause) {
            super(message, cause);
        }

        public ImageUploadException(Throwable cause) {
            super(cause);
        }
    }

    public class ImageDeleteException extends Exception {
        private static final long serialVersionUID = 6295717443044894321L;

        public ImageDeleteException() {
        }

        public ImageDeleteException(String message) {
            super(message);
        }

        public ImageDeleteException(String message, Throwable cause) {
            super(message, cause);
        }

        public ImageDeleteException(Throwable cause) {
            super(cause);
        }
    }
}
