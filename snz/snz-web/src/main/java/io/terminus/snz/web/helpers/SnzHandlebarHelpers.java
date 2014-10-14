package io.terminus.snz.web.helpers;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.google.common.base.Objects;
import io.terminus.common.utils.MapBuilder;
import io.terminus.common.utils.NumberUtils;
import io.terminus.common.utils.Splitters;
import io.terminus.pampas.engine.handlebars.HandlebarEngine;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Desc: snz的HandlebarHelper
 * Mail: houly@terminus.io
 * author: Hou Luyao
 * Date: 14-7-29.
 */
@Component
public class SnzHandlebarHelpers {
    @Autowired
    private HandlebarEngine handlebarEngine;

    @PostConstruct
    public void init() {
        handlebarEngine.registerHelper("isEmpty", new Helper<Object>() {
            @Override
            public CharSequence apply(Object obj, Options options) throws IOException {
                if (obj == null || obj.equals("")) {
                    return options.fn();
                } else {
                    return options.inverse();
                }
            }
        });

        handlebarEngine.registerHelper("get", new Helper<Object>() {
            @Override
            public CharSequence apply(Object context, Options options) throws IOException {
                Object collections;
                Object param = options.param(0);
                if (context instanceof List || context instanceof Map) {
                    collections = context;
                }
                else {
                    collections = Splitters.COMMA.splitToList(String.valueOf(context));
                }

                if (collections == null || param == null) {
                    return null;
                }

                if(collections instanceof List){
                    return ((List)collections).get(Integer.valueOf(param.toString())).toString();
                }
                else{
                    return ((Map)collections).get(param).toString();
                }
            }
        });

        handlebarEngine.registerHelper("contain", new Helper<Object>() {
            @Override
            public CharSequence apply(Object context, Options options) throws IOException {
                Object collections;
                Object param = options.param(0);
                if (context instanceof Collection || context instanceof Map ) {
                    collections = context;
                }
                else{
                    collections = Splitters.COMMA.splitToList(String.valueOf(context));
                }

                if (collections == null || param == null) {
                    return options.fn();
                }

                if(collections instanceof Collection){
                    if(((Collection)collections).contains(param)){
                        return options.fn();
                    }
                    else{
                        return options.inverse();
                    }
                }
                else {
                    Map map = (Map)collections;
                    if(map.keySet().contains(param) || map.values().contains(param)){
                        return options.fn();
                    }
                    else{
                        return options.inverse();
                    }
                }
            }
        });

        handlebarEngine.registerHelper("containAny", new Helper<Object>() {
            @Override
            public CharSequence apply(Object context, Options options) throws IOException {
                Object collections;
                if (context instanceof Collection || context instanceof Map) {
                    collections = context;
                } else {
                    collections = Splitters.COMMA.splitToList(String.valueOf(context));
                }

                if (collections == null || options.params == null || options.params.length == 0) {
                    return options.fn();
                }

                if(collections instanceof Collection){
                    for (Object param : options.params) {
                        for (String p : Splitters.COMMA.splitToList((String)param)) {
                            if (((Collection)collections).contains(p)) {
                                return options.fn();
                            }
                        }
                    }
                    return options.inverse();
                } else {
                    Map map = (Map)collections;
                    for (Object param : options.params) {
                        for (String p : Splitters.COMMA.splitToList((String) param)) {
                            if (map.keySet().contains(param) || map.values().contains(param)) {
                                return options.fn();
                            }
                        }
                    }
                    return options.inverse();
                }
            }
        });

        /**
         *
         * 传参 a, b, c
         * 然后a是一个"2014-08-09"或者''2014-08-09 12:00:00"
         * b的值可以是"dayStart",''now","dayEnd","2014-08-09",''2014-08-09 12:00:00"中的一个
         * 如果b是dayStart, a就和当天的0点0分比较
         * 如果b是now, a就和现在时间比较
         * 如果b是dayEnd, a就和第二天的0点0分比较
         * 如果b是一个date，a就和b比较
         * 第三个参数：
         * 设置时间的比较级别：s:秒,m:分,h:小时,d:天
         */
        handlebarEngine.registerHelper("comDate", new Helper<Object>() {
            @Override
            public CharSequence apply(Object context, Options options) throws IOException {
                //获取参数
                Object param1 = context;
                Object param2 = options.param(0);

                //默认按照秒比较时间
                String comType = Objects.firstNonNull((String)options.param(1) , "s");

                if(param1 == null || param2 == null){
                    return options.fn();
                }else{
                    Date comTime1 = new DateTime(param1).toDate();//Date)param1;

                    String param = (String)param2;
                    Date comTime2;
                    if(Objects.equal(param , "dayStart")){
                        //compare1与当天的0点0分比较

                        comTime2 = DateTime.now().withTime(0 , 0, 0, 0).toDate();
                    }else if(Objects.equal(param , "now")){
                        //compare1就和现在时间比较

                        comTime2 = DateTime.now().toDate();
                    }else if(Objects.equal(param , "dayEnd")){
                        //compare1就和第二天的0点0分比较

                        comTime2 = DateTime.now().plusDays(1).withTime(0 , 0, 0, 0).toDate();
                    }else{
                        //compare1就和compare2比较

                        comTime2 = DateTime.parse(param).toDate();
                    }

                    if(compareTime(comTime1 , comTime2 , comType)){
                        return options.inverse();
                    }else{
                        return options.fn();
                    }
                }
            }
        });

        /**
         * 格式化金钱 第一个参数格式化到得金额，第二个是保留小数的位数，第三个是传值为空时返回
         * 什么数据，E-empty string（默认），Z-zero
         * {{formatPrice 变量名 "W" 2 E}}
         */
        handlebarEngine.registerHelper("formatPrice", new Helper<Number>() {
            Map<String, Integer> sdfMap = MapBuilder.<String, Integer>of().put(
                    "W", 1000000,
                    "Q", 100000,
                    "B", 10000,
                    "Y", 100
            ).map();
            @Override
            public CharSequence apply(Number price, Options options) throws IOException {
                String defaultValue = "E";
                if (options.params != null && options.params.length ==3) {
                    defaultValue = options.param(2).toString();
                }
                if (price == null) return defValue(defaultValue);

                if (options.params == null || options.params.length == 0){
                    return NumberUtils.formatPrice(price);
                }
                else if( options.params.length == 1){
                    Object param1 = options.param(0);
                    int divisor = sdfMap.get(param1.toString().toUpperCase()) == null?1:sdfMap.get(param1.toString().toUpperCase());
                    return getDecimalFormat(2).format(price.doubleValue()/divisor);
                }
                else if (options.params.length == 2 || options.params.length ==3 ){
                    Object param1 = options.param(0);
                    Object param2 = options.param(1);
                    int divisor = sdfMap.get(param1.toString().toUpperCase()) == null?1:sdfMap.get(param1.toString().toUpperCase());
                    return getDecimalFormat(Integer.valueOf(param2.toString())).format(price.doubleValue() / divisor);
                }
                return "";
            }
        });

        /**
         * Usage: <span>{{formatDecimal 10101.2 2}}</span> ->
         *        <span> 10,101.20 </span>
         * 第一个参数是传入的数，第二是保留小数位
         * 默认不保留小数位
         */
        handlebarEngine.registerHelper("formatDecimal", new Helper<Number>() {

            @Override
            public CharSequence apply(Number number, Options options) throws IOException {
                if (number == null) {
                    return "0";
                }
                DecimalFormat df;
                if (options.params == null || options.params.length == 0) {
                    df = getDecimalFormat(0);
                } else {
                    df = getDecimalFormat(Integer.valueOf(options.params[0].toString()));
                }
                return df.format(number.doubleValue());
            }
        });
    }

    private String defValue(String defaultValue) {
        if (defaultValue.toUpperCase().equals("E")) {
            return "";
        }
        if (defaultValue.toUpperCase().equals("Z")) {
            return "0";
        }
        return "";
    }

    private DecimalFormat getDecimalFormat(Integer digits){
        DecimalFormat decimalFormat = new DecimalFormat();
        Integer fractionDigits = digits == null? 2:digits;
        decimalFormat.setMaximumFractionDigits(fractionDigits);
        decimalFormat.setMinimumFractionDigits(fractionDigits);
        return decimalFormat;
    }

    /**
     * 比较开始时间以及结束时间之间的大小（按照比较级别）
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param comType   比较级别
     * @return  Boolean
     * 返回是否大于（true：大于, false：小于）
     */
    private Boolean compareTime(Date startTime , Date endTime, String comType){
        Boolean result = false;
        if(Objects.equal(comType , "s")){
            //按照秒比较
            result = startTime.after(endTime);
        }else if(Objects.equal(comType , "m")){
            //按照分钟比较
            result = Minutes.minutesBetween(new DateTime(startTime), new DateTime(endTime)).getMinutes() > 0;
        }else if(Objects.equal(comType , "h")){
            //按照小时比较
            result = Hours.hoursBetween(new DateTime(startTime), new DateTime(endTime)).getHours() > 0;
        }else if(Objects.equal(comType , "d")){
            //按照天比较
            result = Days.daysBetween(new DateTime(startTime), new DateTime(endTime)).getDays() > 0;
        }

        return result;
    }

}
