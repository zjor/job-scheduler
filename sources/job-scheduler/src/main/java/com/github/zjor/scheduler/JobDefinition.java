package com.github.zjor.scheduler;

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
import javax.persistence.Table;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Table(name = "job_definitions")
public class JobDefinition extends Model {

    /**
     * Defines action to be executed.
     * ```
     * {
     *   "type": "constant",
     *   "value": "qotd"
     * }
     * ```
     * Types may be extended in the future to support dynamic source code loading.
     */
    @Type(type = "jsonb")
    @Column(name = "action", columnDefinition = "jsonb", nullable = false)
    private String action;

    /**
     * Map of key-values to be passed to the execution context.
     */
    @Type(type = "jsonb")
    @Column(name = "arguments", columnDefinition = "jsonb")
    private String arguments;

    /**
     * Schedule definition.
     * ```
     * {
     *   "type": "cron",
     *   "value": "1-3 * * * *"
     * }
     * ```
     * Set of types may be extended in the future.
     */
    @Type(type = "jsonb")
    @Column(name = "schedule", columnDefinition = "jsonb", nullable = false)
    private String schedule;

    /**
     * Defines what needs to be done with the output of the action.
     * ```
     * {
     *   "type": "http",
     *   "value": {
     *     "url": "...",
     *     "auth": {
     *       "type": "basic",
     *       "login": "alice",
     *       "password": "s3cr3t"
     *     }
     *   }
     * }
     * ```
     * Other types may be supported in the future, e.g. "mqtt", "log", "db".
     * Array of output is supported to fan-out the result
     */
    @Type(type = "jsonb")
    @Column(name = "output", columnDefinition = "jsonb", nullable = false)
    private String output;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Data
    @Builder
    public static class JobAction {
        public final String type;
        public final String value;
    }
    //TODO: type checking for job definition fields
    //TODO: provide convenient builder methods and shortcuts
    //TODO: transparent JSON mapping to string

}