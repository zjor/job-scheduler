package com.github.zjor.scheduler.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Table(name = "job_outputs")
public class JobOutput extends Model {

    public enum OutputType {
        HTTP, LOG
    }

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private JobDefinition job;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private OutputType type;

    /*
     *     "url": "...",
     *     "auth": {
     *       "type": "basic",
     *       "login": "alice",
     *       "password": "s3cr3t"
     *     }
     */
    @Type(type = "jsonb")
    @Column(name = "definition", columnDefinition = "jsonb")
    private String definition;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    //TODO: formatter may be here

}
