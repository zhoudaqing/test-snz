/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Author:  <a href="mailto:jlchen.cn@gmail.com">jlchen</a>
 * Date: 2012-09-04
 */
@Component
public class MessageSources implements MessageSource {
    public static final Locale DEFAULT_LOCALE = Locale.CHINA;
    private final MessageSource messageSource;

    @Autowired(required = false)
    public MessageSources(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String get(String code) {
        return get(code, new Object[0]);
    }

    public String get(String code, Object... args) {
        if (messageSource == null) {
            return code;
        }
        return messageSource.getMessage(code, args, code, DEFAULT_LOCALE);
    }

    /**
     * Try to resolve the message. Return default message if no message was found.
     *
     * @param code           the code to lookup up, such as 'calculator.noRateSet'. Users of
     *                       this class are encouraged to base message names on the relevant fully
     *                       qualified class name, thus avoiding conflict and ensuring maximum clarity.
     * @param args           array of arguments that will be filled in for params within
     *                       the message (params look like "{0}", "{1,date}", "{2,time}" within a message),
     *                       or <code>null</code> if none.
     * @param defaultMessage String to return if the lookup fails
     * @param locale         the Locale in which to do the lookup
     * @return the resolved message if the lookup was successful;
     * otherwise the default message passed as a parameter
     * @see java.text.MessageFormat
     */
    @Override
    public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        if (messageSource == null) {
            return code;
        }
        return messageSource.getMessage(code, args, defaultMessage, locale);
    }

    /**
     * Try to resolve the message. Treat as an error if the message can't be found.
     *
     * @param code   the code to lookup up, such as 'calculator.noRateSet'
     * @param args   Array of arguments that will be filled in for params within
     *               the message (params look like "{0}", "{1,date}", "{2,time}" within a message),
     *               or <code>null</code> if none.
     * @param locale the Locale in which to do the lookup
     * @return the resolved message
     * @throws org.springframework.context.NoSuchMessageException if the message wasn't found
     * @see java.text.MessageFormat
     */
    @Override
    public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
        if (messageSource == null) {
            return code;
        }
        return messageSource.getMessage(code, args, locale);
    }

    /**
     * Try to resolve the message using all the attributes contained within the
     * <code>MessageSourceResolvable</code> argument that was passed in.
     * <p>NOTE: We must throw a <code>NoSuchMessageException</code> on this method
     * since at the time of calling this method we aren't able to determine if the
     * <code>defaultMessage</code> property of the resolvable is null or not.
     *
     * @param resolvable value object storing attributes required to properly resolve a message
     * @param locale     the Locale in which to do the lookup
     * @return the resolved message
     * @throws org.springframework.context.NoSuchMessageException if the message wasn't found
     * @see java.text.MessageFormat
     */
    @Override
    public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
        return messageSource.getMessage(resolvable, locale);
    }
}
