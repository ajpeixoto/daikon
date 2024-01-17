// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.i18n;

import org.talend.daikon.exception.ExceptionContext;
import org.talend.daikon.exception.TalendRuntimeException;
import org.talend.daikon.exception.error.CommonErrorCodes;

public class TranslatableImpl implements Translatable {

    private transient I18nMessages i18nMessages;

    @Override
    public void setI18nMessageFormatter(I18nMessages i18nMessages) {
        this.i18nMessages = i18nMessages;
    }

    public I18nMessages getI18nMessageFormatter() {
        if (i18nMessages == null) {
            i18nMessages = createI18nMessageFormater();
        }
        return i18nMessages;
    }

    /**
     * This uses the globalContext static variable and the current Class package to find the resource bundle named
     * messages.properties (it also look into inherited class, if no key is found)
     * 
     * @return the already set I18nMessages or a newly created one base on the current Class package.
     */
    protected I18nMessages createI18nMessageFormater() {
        return GlobalI18N.getI18nMessageProvider().getI18nMessages(this.getClass());
    }

    @Override
    public String getI18nMessage(String key, Object... arguments) {
        I18nMessages i18nMessageFormater = getI18nMessageFormatter();
        if (i18nMessageFormater != null) {
            return i18nMessageFormater.getMessage(key, arguments);
        } else {
            throw new TalendRuntimeException(CommonErrorCodes.MISSING_I18N_TRANSLATOR, ExceptionContext.build().put("key", key)); //$NON-NLS-1$
        }
    }
}
