package com.github.zjor.scheduler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@MappedSuperclass
public abstract class Model implements Serializable {

    @Id
    @GeneratedValue(generator = "short-id")
    @GenericGenerator(name = "short-id", strategy = "com.github.zjor.hibernate.ext.ShortIdGenerator")
    @Column(name = "id", unique = true, length = 36)
    private String id;

}
