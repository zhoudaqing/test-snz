package io.terminus.snz.web.controllers.file;

/**
 * Author:  <a href="mailto:jlchen.cn@gmail.com">jlchen</a>
 * Date: 2013-07-27
 */
public class FileDeleteException extends Exception {
    private static final long serialVersionUID = 6295717443044894321L;

    public FileDeleteException() {
    }

    public FileDeleteException(String message) {
        super(message);
    }

    public FileDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileDeleteException(Throwable cause) {
        super(cause);
    }
}
