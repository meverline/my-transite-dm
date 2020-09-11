package org.dm.transit.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "dm_userJobs")
public class UserJob implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "UUID", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator( name = "native", strategy = "native")
    private long uuid = -1;

    @Column(name = "USER", nullable = false)
    private String user;

    @Column(name = "JOB_DATA", nullable = false)
    private String jsonJobData;

    @Column(name = "JOB_UUID", nullable = false)
    private String jobUuid;

    @JsonGetter("uuid")
    public long getUuid() {
        return uuid;
    }

    @JsonSetter("uuid")
    public void setUuid(long uuid) {
        this.uuid = uuid;
    }

    @JsonGetter("user")
    public String getUser() {
        return user;
    }

    @JsonSetter("user")
    public void setUser(String user) {
        this.user = user;
    }

    @JsonGetter("jobData")
    public String getJsonJobData() {
        return jsonJobData;
    }

    @JsonSetter("jobData")
    public void setJsonJobData(String jsonJobData) {
        this.jsonJobData = jsonJobData;
    }

    @JsonGetter("jobUUID")
    public String getJobUuid() {
        return jobUuid;
    }

    @JsonSetter("jobUUID")
    public void setJobUuid(String jobUuid) {
        this.jobUuid = jobUuid;
    }
}
