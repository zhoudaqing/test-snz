/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.handlebars;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import io.terminus.common.utils.JsonMapper;
import io.terminus.common.utils.MapBuilder;
import io.terminus.common.utils.NumberUtils;
import io.terminus.common.utils.Splitters;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 13-11-22
 */
@Component
public class HandlebarHelpers {
    @Autowired
    private HandlebarEngine handlebarEngine;

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.###");

    private static final Splitter splitter = Splitter.on(",");

    @PostConstruct
    public void init() {
        handlebarEngine.registerHelper("assign", new Helper<String>() {
            @Override
            public CharSequence apply(String name, Options options) throws IOException {
                CharSequence finalValue = options.apply(options.fn);
                options.context.data(name, finalValue.toString().trim());
                return null;
            }
        });

        handlebarEngine.registerHelper("json", new Helper<Object>() {
            @Override
            public CharSequence apply(Object context, Options options) throws IOException {
                return JsonMapper.nonEmptyMapper().toJson(context);
            }
        });

        handlebarEngine.registerHelper("match", new Helper<String>() {
            @Override
            public CharSequence apply(String regEx, Options options) throws IOException {
                Pattern pat = Pattern.compile(regEx);
                Matcher mat = pat.matcher((String) options.param(0));
                if (mat.find())
                    return options.fn();
                else
                    return options.inverse();
            }
        });
        /**
         * 大于
         */
        handlebarEngine.registerHelper("gt", new Helper<Object>() {
            @Override
            public CharSequence apply(Object source, Options options) throws IOException {

                long _source;
                if (source instanceof Long) {
                    _source = (Long) source;
                } else if (source instanceof Integer) {
                    _source = (Integer) source;
                } else {
                    _source = Long.parseLong((String) source);
                }

                if (_source > (Integer) options.param(0))
                    return options.fn();
                else
                    return options.inverse();
            }
        });
        handlebarEngine.registerHelper("mod", new Helper<Integer>() {
            @Override
            public CharSequence apply(Integer source, Options options) throws IOException {

                if ((source + 1) % (Integer) options.param(0) == 0)
                    return options.fn();
                else
                    return options.inverse();
            }
        });

        handlebarEngine.registerHelper("size", new Helper<Object>() {
            @Override
            public CharSequence apply(Object context, Options options) throws IOException {
                if (context == null) return "0";
                if (context instanceof Collection) return String.valueOf(((Collection) context).size());
                if (context instanceof Map) return String.valueOf(((Map) context).size());
                return "0";
            }
        });

        handlebarEngine.registerHelper("equals", new Helper<Object>() {
            @Override
            public CharSequence apply(Object source, Options options) throws IOException {

                if (Objects.equal(String.valueOf(source), String.valueOf(options.param(0))))
                    return options.fn();
                else
                    return options.inverse();

            }
        });

        handlebarEngine.registerHelper("formatDate", new Helper<Object>() {
            Map<String, SimpleDateFormat> sdfMap = MapBuilder.<String, SimpleDateFormat>of().put(
                    "gmt", new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy"),
                    "day", new SimpleDateFormat("yyyy-MM-dd"),
                    "default", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            ).map();

            @Override
            public CharSequence apply(Object dateObj, Options options) throws IOException {
                if (dateObj == null) {
                    return "";
                }
                Date date = new DateTime(dateObj).toDate();
                String format = options.param(0, "default");
                if (format.equals("ut")) {
                    return Long.toString(date.getTime());
                }
                if (!sdfMap.containsKey(format)) {
                    sdfMap.put(format, new SimpleDateFormat(format));
                }
                return sdfMap.get(format).format(date);
            }
        });

        handlebarEngine.registerHelper("formatPrice", new Helper<Number>() {

            @Override
            public CharSequence apply(Number price, Options options) throws IOException {
                return NumberUtils.formatPrice(price);
            }
        });

        /**
         * 只保留圆角的部分
         */
        handlebarEngine.registerHelper("innerStyle", new Helper<Object>() {
            @Override
            public CharSequence apply(Object context, Options options) throws IOException {

                if (context == null) {
                    return "";
                }

                StringBuilder ret = new StringBuilder();

                String[] styles = ((String) context).split(";");
                for (String style : styles) { //border-radius,border-top-left-radius,...
                    String key = style.split(":")[0];
                    if (key.endsWith("radius")) {
                        ret.append(style).append(";");
                    }
                }
                return ret;
            }
        });

        handlebarEngine.registerHelper("cIndex", new Helper<Integer>() {

            @Override
            public CharSequence apply(Integer context, Options options) throws IOException {
                return "" + (char) (context + 'A');
            }
        });

        handlebarEngine.registerHelper("formatRate", new Helper<Double>() {
            @Override
            public CharSequence apply(Double rate, Options options) throws IOException {
                return rate == null ? "" : DECIMAL_FORMAT.format(rate / 1000.0);
            }
        });

        handlebarEngine.registerHelper("formatIntegerRate", new Helper<Integer>() {
            @Override
            public CharSequence apply(Integer rate, Options options) throws IOException {
                return rate == null ? "" : DECIMAL_FORMAT.format(rate / 1000.0);
            }
        });

        handlebarEngine.registerHelper("of", new Helper<Object>() {
            @Override
            public CharSequence apply(Object source, Options options) throws IOException {
                if (source == null) {
                    return options.inverse();
                }

                String _source = source.toString();
                String param = options.param(0);
                if (Strings.isNullOrEmpty(param)) {
                    return options.inverse();
                }

                List<String> targets = splitter.splitToList(param);
                if (targets.contains(_source)) {
                    return options.fn();
                }
                return options.inverse();
            }
        });

        handlebarEngine.registerHelper("add", new Helper<Object>() {
            @Override
            public CharSequence apply(Object source, Options options) throws IOException {
                Object param = options.param(0);

                if (source == null && param == null) {
                    return "";
                }

                if (source == null) {
                    return param.toString();
                }

                if (param == null) {
                    return source.toString();
                }

                if (source instanceof Double) {
                    Double first = (Double) source;
                    Double second = (Double) param;
                    return String.valueOf(first + second);
                }

                if (source instanceof Integer) {
                    Integer first = (Integer) source;
                    Integer second = (Integer) param;
                    return String.valueOf(first + second);
                }

                if (source instanceof Long) {
                    Long first = (Long) source;
                    Long second = (Long) param;
                    return String.valueOf(first + second);
                }

                if (source instanceof String) {   // String 约定一定可以转换成Integer
                    Integer first = Integer.parseInt(source.toString());
                    Integer second = Integer.parseInt(param.toString());
                    return String.valueOf(first + second);
                }

                throw new IllegalStateException("incorrect.type");
            }
        });

        handlebarEngine.registerHelper("rget", new Helper<Object>() {
            private final Random random = new Random(System.currentTimeMillis());

            @Override
            public CharSequence apply(Object context, Options options) throws IOException {
                List list;
                if (context instanceof List) {
                    list = (List) context;
                } else {
                    list = Splitters.COMMA.splitToList(String.valueOf(context));
                }
                if (list == null || list.isEmpty()) {
                    return null;
                }
                return list.get(random.nextInt(list.size())).toString();
            }
        });
    }
}
