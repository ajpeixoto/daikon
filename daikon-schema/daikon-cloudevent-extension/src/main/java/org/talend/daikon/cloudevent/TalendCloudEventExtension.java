package org.talend.daikon.cloudevent;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
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

    private static final Set<String> KEY_SET = Collections
            .unmodifiableSet(new HashSet<>(Arrays.asList(TENANTID, CORRELATIONID, OWNERID, USERID, CLIENTID, DESCRIPTION)));

    private String tenantid;

    private String correlationid;

    private String ownerid;

    private String userid;

    private String clientid;

    private String description;

    @Override
    public void readFrom(CloudEventExtensions cloudEventExtensions) {
        Object tp = cloudEventExtensions.getExtension(TENANTID);
        if (tp != null) {
            this.tenantid = tp.toString();
        }
        Object ts = cloudEventExtensions.getExtension(CORRELATIONID);
        if (ts != null) {
            this.correlationid = ts.toString();
        }
        Object to = cloudEventExtensions.getExtension(OWNERID);
        if (to != null) {
            this.ownerid = to.toString();
        }
        Object tu = cloudEventExtensions.getExtension(USERID);
        if (tu != null) {
            this.userid = tu.toString();
        }
        Object tc = cloudEventExtensions.getExtension(CLIENTID);
        if (tc != null) {
            this.clientid = tc.toString();
        }
        Object td = cloudEventExtensions.getExtension(DESCRIPTION);
        if (td != null) {
            this.description = td.toString();
        }
    }

    @Override
    public Object getValue(String key) throws IllegalArgumentException {
        switch (key) {
        case TENANTID:
            return this.tenantid;
        case CORRELATIONID:
            return this.correlationid;
        case OWNERID:
            return this.ownerid;
        case USERID:
            return this.userid;
        case CLIENTID:
            return this.clientid;
        case DESCRIPTION:
            return this.description;
        }
        throw ExtensionUtils.generateInvalidKeyException(this.getClass(), key);
    }

    @Override
    public Set<String> getKeys() {
        return KEY_SET;
    }

    public String getTenantid() {
        return tenantid;
    }

    public void setTenantid(String tenantid) {
        this.tenantid = tenantid;
    }

    public String getCorrelationid() {
        return correlationid;
    }

    public void setCorrelationid(String correlationid) {
        this.correlationid = correlationid;
    }

    public String getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TalendCloudEventExtension.class.getSimpleName() + "[", "]").add("tenantid=" + tenantid)
                .add("correlationid='" + correlationid + "'").add("ownerid='" + ownerid + "'").add("userid='" + userid + "'")
                .add("clientid='" + clientid + "'").add("description='" + description + "'").toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        org.talend.daikon.cloudevent.TalendCloudEventExtension that = (org.talend.daikon.cloudevent.TalendCloudEventExtension) o;
        return Objects.equals(getCorrelationid(), that.getCorrelationid()) && Objects.equals(getOwnerid(), that.getOwnerid())
                && Objects.equals(getClientid(), that.getClientid()) && Objects.equals(getUserid(), that.getUserid())
                && Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTenantid(), getCorrelationid(), getOwnerid(), getUserid(), getClientid(), getDescription());
    }
}
