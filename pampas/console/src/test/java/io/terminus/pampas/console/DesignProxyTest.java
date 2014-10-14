/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.console;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.net.HttpHeaders;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-6-11
 */
public class DesignProxyTest {
    @Test
    public void test() {
        HttpRequest httpRequest = new HttpRequest(HttpRequest.append("http://www.rrs.cn/assets/scripts/app2.js"), "GET");
//        copyRequestHeader(httpRequest, request,
//                HttpHeaders.ACCEPT, HttpHeaders.ACCEPT_ENCODING, HttpHeaders.ACCEPT_LANGUAGE, HttpHeaders.CONNECTION, HttpHeaders.USER_AGENT);
        httpRequest.header(HttpHeaders.ACCEPT, "*/*");
        httpRequest.header(HttpHeaders.ACCEPT_ENCODING, "gzip,deflate,sdch");
        httpRequest.header(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.8,en;q=0.6");
        httpRequest.header(HttpHeaders.CONNECTION, "keep-alive");
//        httpRequest.header(HttpHeaders.USER_AGENT, "");
        System.out.println(httpRequest.body());
    }

    private void copyRequestHeader(HttpRequest httpRequest, HttpServletRequest request, String... headers) {
        for (String header : headers) {
            httpRequest.header(header, request.getHeader(header));
        }
    }

    @Test
    public void testEncoding() throws UnsupportedEncodingException {
        String origin = "\u001F�\b\u0003�Xms\u0013�\u0015�,���� yb�J\u0016P,k�)�\u0004&&a�;I��aV�WҚ}c��%�x�!\n" +
                "/S�3\n" +
                "\t%�)$)�0\u0010Hh\u0003š�1�J���9���V�6�\u001F:�x��w�9����^�_9�����O\u001E%mf[���\u000EO\u001F�\"RIQޭN)ʑ�#��c�'�IE.�\u0019��:S��oKDj3��\u0014���ȝ���-e���E)\u0015d������\u00063��<���9]�r\u0002u\u001B\u0019���q�*\u0011KsZ�D\u001D`���f��e:g\b[��*1�e�\u001E\u0004\u0012���J\u0001[�hЦ�I��Ӧ*)�\u0016(Z\u0010P\u0016(��b�-�9����:2gU\u0012�;�4Z`ez������\bE���7\\c����j�*�m0�\u0016G&$�:C\u0017\f���\u0012,�Q\u000Fܟ����9O�<UZ,��A�5R�L�65[mV#��ǽ.��-PL�\fj�.i�f\u0012�J��0�t�/M�\u0015��\b5A\u0005�\u000B_�sb/\u0016\u0002@�|3W�b���i��f1U���q���D\u0002_\u001F\u0006Ŵ�\u0016\n" +
                "\u0010HW��Zh}]Ѹ������:�~�\u0001�b+�\u0018�U��Ӌf�\u001B곓\n" +
                "���9\\���[�4�XR��m�LӮ�P6�\u0012\u0012S�o�]�h:3]'>�3�\u001EP\u007F�ԩ�q�j�����\u0001���\\�\u001E�+\u0007�_�����x\u0019v���\u0001F\n" +
                "K�(�������y�\u001CD�MY�\u00055<7`\u0018\u0015AذM\u001C|\u0019�\u000E��4Y\u001C\u0019�wh�E\u000E�4���\f�tZ��˘k�*e�b�]�\u0014n�?Z����\u007Fc-z����o{�\u001EC|V�O��\u0011\u0016c@}�2%\u000E�\u001D��cs9�\"�_n�5������k(?��*\u001CQM_x�\n" +
                "\\�x!�d,�\u001C2\u0017��,�`��Y\u0001��0\u0017�a��D�5+��D���٠J\u0003�-�FG��!���l`��쳒��$z��\u007F���>�\u0011x\u0013�\u007F�o}��Q��\u0007ju\\�H�\u0018<�\u001A��lh����3&ŀ\f�S�\u0007@\f��6��\u007Fd���+\u001B\u000F>x�\u0011Y�\b\u0013@\u0015��0\u0015�\\�f�\u000F�:\u0016zXۨ~\u0006\t�0��Gl����\u0011��rkd��5�[\u0014*���T\"\u0010\u000F�-S?�Jsڼ&r�\u00065ʇ�[dm3���\fs�\u007F�\u001F�ŵ���zW�L-��l\u000E\u001D\u001Dx��Uc��kikNY��jUH(ݵ\\����\u001F:�70�\u0014�\u007Fi\u0007h�\n" +
                "��(\n" +
                "*����\u001A���l�*5B�rg��\\����\n" +
                "u%�\u0017C\u0007�MÀ.\u001A��\u001A\u001C5=[�\u001C\u001C?P.-�m\u001B�e�����4��p�MO�:S�9c�\u000E5#�p\u0017��K�\u0010�&\u0015N+A�e8O�y�\u000E�\u0007I���bֹ��N\u0007n��TD��F��\u0010TW�\t<ߪH�]��*MN�ނ�]���+U\"�����h�Kҿ|���\u000F{��{7/ǒ�\"H��U��3�R�\u0002e�lH��RE>$We�tx7�\f2�P���W�\b�;��x�2\u0012s��\u001D(����\u0011ه�a��\f\u001D�\u0012�#���ł̇�\u0088�\u0019?��.'�r����M�����V\u0005���(ّ`�E\u0004UN�����wM��k�2v�<J��c��B�\u0006P��\u0011(�ܘ\u001A)�]\u0018M�g�m�x�\u001A5pu��_0_s\u0002\u0013��6��\u0005,M\u0018�b.(����\t(U�\u0081��,��\u00010g�a��\u0016j�A\u001El/�L�4�S\u0014��ӭޭ��ʣ�k�E߯l\\{\u0006�\n" +
                "W\u000FmH\u0019�\u001E\u000EJ]\u0006O!Q�}r���\u0001��\u0013KhV^\b���5�@Q��\t\\e�K8�`�$�%���\u0017Х���*Cw\u0001�\u0005r\u0013�J%sk*�^��#�U�#�\bGXQ�QA�hX�\u001C?J4� M��vI��\u001A4g� V(1\u0003⸌��'��Rq1�?}�}�rBl\n" +
                "\f�r��\u0010'��b�ET�T�`��D�_l<�\u0003�?�\u001D¼�������\\\u001A����(;jQ\\\u001E^8n\u0014��\u001C�[҈�{\u000F44�(\u001B\u000F?�\u0001GLR�+�7��R����߯�����D�J�I辔��<:�\u007F�z��U��7�Õ|�\u0003]����аۓ�<{y/!��B�R\u0005C�2vC\n" +
                "���I�.\u0011SwO<�C��5�\u0018\u000F��(\u0012�L�Di~oO�D!�^$\u0010\u000F�]^B�a�\u0018�|&d�;��7\u0012\u0003\u0012\u0002\n" +
                "`w2\u0018wJx\u001F����\u0011X�/���i�ѷ����ʟ6/����k�\n" +
                "\u0005�\u0011�=H\u0018=�D��\u001B��$����{�����'w������@����\u0018-��ݺ\n" +
                "#Y:���������-\u007F��kE��ód��t\u007F�:��7\u001D)� h�Ы,��X\u001BmAogY^Q������\u000F��ׅ��Ρ�5\u001C .u|�i�%�\u0013�$��\u0006�ܐS�;�{_܌\u001E��_[\u0011�o.\u007F����Q� �:!<�n�?�\u001A]�������+\u0002*�����\u0019*a~Hɾ}d�����r��%�@Р��\u0016$\u0005����\u001A��iݍ?<�V>��\\�}�0�AЯ?�\u000Fn�\"\u0019�åo���Ÿ�'W@�\u0016\b������B�\u001Ez�\u0005\u000B�^�`�f�C�B\u0016�n���\u0010\t�34�I_�\u0005��\u000E�\u001Eb\fB]\u0007\u0013�7-�(K���*'���\u0001/e.�T�O�li�Ap'v����NL\u001Fc�;��?`�\u0004��\f�X\u0018�\n" +
                "�ٶ\u000F\n" +
                "\u0011\u001B�� �Ƣ>+J���z�>ݒ\u0010�4B&����6��]8\u0004�!�A%\u0005��\u0002����\u001F;\u0006����G�-���D\u001D��h\u0010��x����.�r۔��P�?9$�\u0013�So����_g;E�[I�\u001B ��enI�p��$��\u0017�\u000E�X��\u001FN>�cd�\u0010�\u0007��q*��O\u00068�\u0017��\u001D���\u001EW�\u0011\u0012\u0014\u00067r\u0018\u001F�l��a���\u007F-�\u0017�\bi${\\Rl�<^�qzL�3\u0018$�b�1'B\u001F\u00162�\u0001���v���J���X[�!�p�\"�b)�� (�b���`���\u0001�\u0014�.!\u0016";

        String target = new String(origin.getBytes("utf-8"), "utf-8");
        System.out.println(target);
    }
}
