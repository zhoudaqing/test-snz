/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.webc.utils;

import io.terminus.common.utils.MapBuilder;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-12
 */
public class MimeTypes {
    private static final String DEFAULT_TYPE = "application/octet-stream";

    private static final Map<String, String> TYPE_MAP = MapBuilder.<String, String>of()
            .put("html", "text/html")
            .put("htm", "text/html")
            .put("shtml", "text/html")
            .put("css", "text/css")
            .put("xml", "text/xml")
            .put("gif", "image/gif")
            .put("jpeg", "image/jpeg")
            .put("jpg", "image/jpeg")
            .put("js", "application/x-javascript")
            .put("atom", "application/atom+xml")
            .put("rss", "application/rss+xml")
            .put("mml", "text/mathml")
            .put("txt", "text/plain")
            .put("jad", "text/vnd.sun.j2me.app-descriptor")
            .put("wml", "text/vnd.wap.wml")
            .put("htc", "text/x-component")
            .put("png", "image/png")
            .put("tif", "image/tiff")
            .put("tiff", "image/tiff")
            .put("wbmp", "image/vnd.wap.wbmp")
            .put("ico", "image/x-icon")
            .put("jng", "image/x-jng")
            .put("bmp", "image/x-ms-bmp")
            .put("svg", "image/svg+xml")
            .put("svgz", "image/svg+xml")
            .put("webp", "image/webp")
            .put("jar", "application/java-archive")
            .put("war", "application/java-archive")
            .put("ear", "application/java-archive")
            .put("hqx", "application/mac-binhex40")
            .put("doc", "application/msword")
            .put("pdf", "application/pdf")
            .put("ps", "application/postscript")
            .put("eps", "application/postscript")
            .put("ai", "application/postscript")
            .put("rtf", "application/rtf")
            .put("xls", "application/vnd.ms-excel")
            .put("ppt", "application/vnd.ms-powerpoint")
            .put("wmlc", "application/vnd.wap.wmlc")
            .put("kml", "application/vnd.google-earth.kml+xml")
            .put("kmz", "application/vnd.google-earth.kmz")
            .put("7z", "application/x-7z-compressed")
            .put("cco", "application/x-cocoa")
            .put("jardiff", "application/x-java-archive-diff")
            .put("jnlp", "application/x-java-jnlp-file")
            .put("run", "application/x-makeself")
            .put("pl", "application/x-perl")
            .put("pm", "application/x-perl")
            .put("prc", "application/x-pilot")
            .put("pdb", "application/x-pilot")
            .put("rar", "application/x-rar-compressed")
            .put("rpm", "application/x-redhat-package-manager")
            .put("sea", "application/x-sea")
            .put("swf", "application/x-shockwave-flash")
            .put("sit", "application/x-stuffit")
            .put("tcl", "application/x-tcl")
            .put("tk", "application/x-tcl")
            .put("der", "application/x-x509-ca-cert")
            .put("pem", "application/x-x509-ca-cert")
            .put("crt", "application/x-x509-ca-cert")
            .put("xpi", "application/x-xpinstall")
            .put("xhtml", "application/xhtml+xml")
            .put("zip", "application/zip")
            .put("bin", "application/octet-stream")
            .put("exe", "application/octet-stream")
            .put("dll", "application/octet-stream")
            .put("deb", "application/octet-stream")
            .put("dmg", "application/octet-stream")
            .put("eot", "application/octet-stream")
            .put("iso", "application/octet-stream")
            .put("img", "application/octet-stream")
            .put("msi", "application/octet-stream")
            .put("msp", "application/octet-stream")
            .put("msm", "application/octet-stream")
            .put("mid", "audio/midi")
            .put("midi", "audio/midi")
            .put("kar", "audio/midi")
            .put("mp3", "audio/mpeg")
            .put("ogg", "audio/ogg")
            .put("m4a", "audio/x-m4a")
            .put("ra", "audio/x-realaudio")
            .put("3gpp", "video/3gpp")
            .put("3gp", "video/3gpp")
            .put("mp4", "video/mp4")
            .put("mpeg", "video/mpeg")
            .put("mpg", "video/mpeg")
            .put("mov", "video/quicktime")
            .put("webm", "video/webm")
            .put("flv", "video/x-flv")
            .put("m4v", "video/x-m4v")
            .put("mng", "video/x-mng")
            .put("asx", "video/x-ms-asf")
            .put("asf", "video/x-ms-asf")
            .put("wmv", "video/x-ms-wmv")
            .put("avi", "video/x-msvideo")
            .put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            .put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
            .put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation")
            .map();

    public static String getType(String suffix) {
        String type = TYPE_MAP.get(suffix);
        return type == null ? DEFAULT_TYPE : type;
    }
}
