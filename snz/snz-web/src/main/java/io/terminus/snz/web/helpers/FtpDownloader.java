package io.terminus.snz.web.helpers;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * FTP 文件下载客户端
 *
 * @author wanggen on 14-8-14.
 */
@Data
@Slf4j
public class FtpDownloader {

    private String host;
    private int port;
    private String username;
    private String password;

    private FTPClient ftpClient;

    /**
     * 初始化 ftp 服务器连接
     *
     * @param host     主机
     * @param port     端口
     * @param username 用户名
     * @param password 密码
     * @throws java.io.IOException 若连接不成功则终止程序
     */
    public FtpDownloader(String host, int port, String username, String password) throws IOException {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        
        ftpClient = new FTPClient();
        ftpClient.connect(host, port);
        boolean login = ftpClient.login(username, password);
        Preconditions.checkState(login);
        if(!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())){
            ftpClient.disconnect();
            return;
        }
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        ftpClient.enterRemotePassiveMode();
        ftpClient.setDefaultTimeout(1000 * 60 * 60);
    }

    /**
     *
     * @param dir
     * @param fileName
     */
    public void copyToLocal(String dir, String fileName) {
        copyToLocal(dir, fileName, "");
    }

    /**
     * 将远程文件按远程文件路径复制到本地
     *
     * @param dir      远程服务器文件夹路径
     * @param fileName 远程服务器文件夹下文件
     * @param localDir 本地文件夹
     */
    public void copyToLocal(String dir, String fileName, String localDir) {
        OutputStream outStream = null;
        try {
            Preconditions.checkState(!Strings.isNullOrEmpty(dir), "The directory must be assigned");
            Preconditions.checkState(!Strings.isNullOrEmpty(fileName), "One file name must be assigned");
            if (!Strings.isNullOrEmpty(localDir)) {
                if(!localDir.endsWith("/"))
                    localDir = localDir + "/";
                File path = new File(localDir);
                if (!path.exists() || !path.isDirectory())
                    Preconditions.checkState(path.mkdirs(), "Create local dir:[%s] failed", path);
            }else{
                localDir="";
            }
            outStream = new FileOutputStream(localDir + fileName);
            boolean correctDir = ftpClient.changeWorkingDirectory(dir);
            Preconditions.checkState(correctDir, "Illegal dir:[%s]", dir);
            boolean correctFileName = ftpClient.retrieveFile(fileName, outStream);
            Preconditions.checkState(correctFileName, "File:[%s] not found in directory:[%s]", fileName, dir);
        } catch (IOException e) {
            log.error("Error path:[{}] or file name:[{}]", dir, fileName, e);
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {}
            }
        }
    }

}
