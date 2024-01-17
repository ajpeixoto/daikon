// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.cloudevent;

import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

import io.cloudevents.CloudEventExtension;
import io.cloudevents.CloudEventExtensions;
import io.cloudevents.core.extensions.impl.ExtensionUtils;

/**
 * This extension embeds context from Talend so that distributed systems can include traces that span an event-driven system.
 *
 * @see <a href=
 * "https://github.com/Talend/policies/blob/master/official/kafka-application/README_event_specification.adoc#talend-cloudevents-extension">https://github.com/Talend/policies/blob/master/official/kafka-application/README_event_specification.adoc#talend-cloudevents-extension</a>
 */
public class TalendCloudEventExtension implements CloudEventExtension {

    /**
     * The key of the {@code tenantid} extension
     */
    public static final String TENANTID = "tenantid";

    /**
     * The key of the {@code correlationid} extension
     */
    public static final String CORRELATIONID = "correlationid";

    /**
     * The key of the {@code ownerid} extension
     */
    public static final String OWNERID = "ownerid";

    /**
     * The key of the {@code userid} extension
     */
    public static final String USERID = "userid";

    /**
     * The key of the {@code clientid} extension
     */
    public static final String CLIENTID = "clientid";

    /**
     * The key of the {@code description} extension
     */
    public static final String DESCRIPTION = "description";

    private static final Set<String> KEY_SET = Set.of(TENANTID, CORRELATIONID, OWNERID, USERID, CLIENTID, DESCRIPTION);

    private String tenantId;

    private String correlationId;

    private String ownerId;

    private String userId;

    private String clientId;

    private String description;

    public TalendCloudEventExtension() {
    }

    public TalendCloudEventExtension(TalendCloudEventExtensionBuilder talendCloudEventExtensionBuilder) {
        this.tenantId = talendCloudEventExtensionBuilder.tenantId;
        this.correlationId = talendCloudEventExtensionBuilder.correlationId;
        this.ownerId = talendCloudEventExtensionBuilder.ownerId;
        this.userId = talendCloudEventExtensionBuilder.userId;
        this.clientId = talendCloudEventExtensionBuilder.clientId;
        this.description = talendCloudEventExtensionBuilder.description;
    }

    @Override
    public void readFrom(CloudEventExtensions cloudEventExtensions) {
        this.tenantId = getExtensionAsString(cloudEventExtensions, TENANTID);
        this.correlationId = getExtensionAsString(cloudEventExtensions, CORRELATIONID);
        this.ownerId = getExtensionAsString(cloudEventExtensions, OWNERID);
        this.userId = getExtensionAsString(cloudEventExtensions, USERID);
        this.clientId = getExtensionAsString(cloudEventExtensions, CLIENTID);
        this.description = getExtensionAsString(cloudEventExtensions, DESCRIPTION);
    }

    private String getExtensionAsString(CloudEventExtensions cloudEventExtensions, String extensionName) {
        return (String) cloudEventExtensions.getExtension(extensionName);
    }

    @Override
    public Object getValue(String key) throws IllegalArgumentException {
        switch (key) {
        case TENANTID:
            return this.tenantId;
        case CORRELATIONID:
            return this.correlationId;
        case OWNERID:
            return this.ownerId;
        case USERID:
            return this.userId;
        case CLIENTID:
            return this.clientId;
        case DESCRIPTION:
            return this.description;
        }
        throw ExtensionUtils.generateInvalidKeyException(this.getClass(), key);
    }

    @Override
    public Set<String> getKeys() {
        return KEY_SET;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getUserId() {
        return userId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TalendCloudEventExtension.class.getSimpleName() + "[", "]").add("tenantid=" + tenantId)
                .add("correlationid='" + correlationId + "'").add("ownerid='" + ownerId + "'").add("userid='" + userId + "'")
                .add("clientid='" + clientId + "'").add("description='" + description + "'").toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        org.talend.daikon.cloudevent.TalendCloudEventExtension that = (org.talend.daikon.cloudevent.TalendCloudEventExtension) o;
        return Objects.equals(getCorrelationId(), that.getCorrelationId()) && Objects.equals(getTenantId(), that.getTenantId())
                && Objects.equals(getOwnerId(), that.getOwnerId()) && Objects.equals(getUserId(), that.getUserId())
                && Objects.equals(getClientId(), that.getClientId()) && Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTenantId(), getCorrelationId(), getOwnerId(), getUserId(), getClientId(), getDescription());
    }

    public static TalendCloudEventExtensionBuilder builder() {
        return new TalendCloudEventExtensionBuilder();
    }

    public static final class TalendCloudEventExtensionBuilder {

        private String tenantId;

        private String correlationId;

        private String ownerId;

        private String userId;

        private String clientId;

        private String description;

        private TalendCloudEventExtensionBuilder() {
        }

        public TalendCloudEventExtensionBuilder tenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public TalendCloudEventExtensionBuilder correlationId(String correlationId) {
            this.correlationId = correlationId;
            return this;
        }

        public TalendCloudEventExtensionBuilder ownerId(String ownerId) {
            this.ownerId = ownerId;
            return this;
        }

        public TalendCloudEventExtensionBuilder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public TalendCloudEventExtensionBuilder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public TalendCloudEventExtensionBuilder description(String description) {
            this.description = description;
            return this;
        }

        public TalendCloudEventExtension build() {
            return new TalendCloudEventExtension(this);
        }
    }
}
