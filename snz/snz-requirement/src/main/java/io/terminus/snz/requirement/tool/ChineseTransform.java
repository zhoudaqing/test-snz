package io.terminus.snz.requirement.tool;

/**
 * Desc:获取中文的首字母
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-08-29.
 */
public class ChineseTransform {
    // 简体中文的编码范围从B0A1（45217）一直到F7FE（63486）
    private static int BEGIN = 45217;
    private static int END = 63486;

    // 按照声母表示，这个表是在GB2312中的出现的第一个汉字，也就是说“啊”是代表首字母a的第一个汉字
    private static char[] charTable = { '啊', '芭', '擦', '搭', '蛾', '发', '噶', '哈',
            '哈', '击', '喀', '垃', '妈', '拿', '哦', '啪', '期', '然', '撒', '塌', '塌',
            '塌', '挖', '昔', '压', '匝', };

    // GB2312码汉字区间十进制表示(26个字母)
    private static int[] table = new int[27];

    // 对应首字母区间表
    private static char[] initialTable = { 'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'H', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'T', 'T', 'W', 'X', 'Y', 'Z', };

    // 初始化
    static {
        for (int i = 0; i < 26; i++) {
            // 得到GB2312码的首字母区间端点表，十进制。
            table[i] = gbValue(charTable[i]);
        }
        // 区间表结尾
        table[26] = END;
    }

    /**
     * 根据一个包含汉字的字符串返回一个汉字拼音首字母的字符串 最重要的一个方法，思路如下：一个个字符读入、判断、输出
     */
    public static String getFirstLetter(String SourceStr) {
        StringBuilder resultBuf = new StringBuilder();
        int StrLength = SourceStr.length();
        int i;
        try {
            for (i = 0; i < StrLength; i++) {
                resultBuf.append(CharToInitial(SourceStr.charAt(i)));
            }
        } catch (Exception e) {
        }
        return resultBuf.toString();
    }

    /**
     * 输入字符,得到他的声母,英文字母返回对应的大写字母,其他非简体汉字返回 '0'
     */
    private static char CharToInitial(char ch) {
        // 对英文字母的处理：小写字母转换为大写，大写的直接返回
        if (ch >= 'a' && ch <= 'z')
            return (char) (ch - 'a' + 'A');
        if (ch >= 'A' && ch <= 'Z')
            return ch;

        // 对非英文字母的处理：转化为首字母，然后判断是否在码表范围内
        // 汉字转换首字母
        int gb = gbValue(ch);

        // 在码表区间之前，直接返回
        if ((gb < BEGIN) || (gb > END))
            return ch;

        int i;
        for (i = 0; i < 26; i++) {
            if ((gb >= table[i]) && (gb < table[i+1]))
                break;
        }

        //补上GB2312区间最右端
        if (gb==END) {
            i=25;
        }
        // 在码表区间中，返回首字母
        return initialTable[i];
    }

    /**
     * 取出汉字的编码 cn 汉字
     * 将一个汉字（GB2312）转换为十进制表示。
     */
    private static int gbValue(char ch) {
        String str = String.valueOf(ch);
        try {
            byte[] bytes = str.getBytes("GB2312");
            if (bytes.length < 2)
                return 0;
            return (bytes[0] << 8 & 0xff00) + (bytes[1] & 0xff);
        } catch (Exception e) {
            return 0;
        }
    }
}
