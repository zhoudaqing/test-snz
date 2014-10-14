/*
 * Copyright (c) 2012 杭州端点网络科技有限公司
 */

package io.terminus.snz.web.controllers.file;

import java.io.Serializable;

public class UploadedFile implements Serializable {

    private static final long serialVersionUID = -38331060124340967L;
    private String name;
    private Integer size;
    private String url;

    private String errorInfo;

    public UploadedFile(String name, String errorInfo) {
        super();
        this.name = name;
        this.errorInfo = errorInfo;
    }

    public UploadedFile( String name, Integer size, String url) {
        super();
        this.name = name;
        this.size = size;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    @Override
    public String toString() {
        return "UploadedFile{" +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", url='" + url + '\'' +
                '}';
    }
}
