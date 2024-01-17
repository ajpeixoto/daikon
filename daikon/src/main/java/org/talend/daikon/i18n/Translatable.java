// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.i18n;

import org.talend.daikon.exception.error.CommonErrorCodes;

public interface Translatable {

    void setI18nMessageFormatter(I18nMessages i18nMessages);

    /**
     * Return the Internationalized messages found from the resource found with the provided baseName from
     * getI18NBaseName()
     * 
     * @param key the key to identify the message
     * @param arguments the arguments that shall be used to format the message using
     * {@link java.text.MessageFormat#format(String, Object...))}
     * @return the formatted string or the key if no message was found
     * @exception org.talend.daikon.exception.TalendRuntimeException thrown with the code
     * {@link CommonErrorCodes#MISSING_I18N_TRANSLATOR} if the
     * I18nMessages that should have been set by {@link #setI18nMessageFormatter(I18nMessages)} is null
     */
    String getI18nMessage(String key, Object... arguments);

}