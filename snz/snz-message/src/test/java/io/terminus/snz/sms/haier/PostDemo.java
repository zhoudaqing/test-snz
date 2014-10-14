package io.terminus.snz.sms.haier;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostDemo {
    public PostDemo() {
    }

    public static void main(String[] args) {
        try {

            String urlstr = "http://10.128.3.249:8080/HttpApi_Simple/submitMessage";

            URL url = new URL(urlstr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Connection", "close");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(5000);

            conn.connect();

            //haier
            String inputXML =
                    "<?xml version=\"1.0\" encoding=\"GBK\"?>\n" +
                    "<CoreSMS>\n" +
                    "  <OperID>gjmy2</OperID>\n" +
                    "  <OperPass>gjmy2</OperPass>\n" +
                    "  <Action>Submit</Action>\n" +
                    "  <Category>0</Category>\n" +
                    "  <Body>\n" +
                    "    <SendTime>20140911145013</SendTime>\n" +
                    "    <AppendID></AppendID>\n" +
                    "    <Message>\n" +
                    "      <DesMobile>18606810903</DesMobile>\n" +
                    "      <Content>短信网关测试120</Content>\n" +
                    "      <SendType></SendType>\n" +
                    "    </Message>\n" +
                    "  </Body>\n" +
                    "</CoreSMS>";

            byte[] b = inputXML.getBytes("GBK");
            OutputStream os = conn.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            dos.write(b);
            dos.flush();
            os.close();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            int r = bis.read();
            while (r >= 0) {
                bos.write(r);
                r = bis.read();
            }
            String outputXML = new String(bos.toByteArray(), "UTF-8");
            is.close();

            conn.disconnect();
            System.out.println("回复: " + outputXML);

        } catch (IOException ex2) {
            ex2.printStackTrace();
        }
    }
}