package com.example.carreg.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * Abstract entity for @Entity objects.
 */
@MappedSuperclass
@Getter
@Setter
public class AbstractEntity {

    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "create_date", nullable = false)
    @CreationTimestamp
    @JsonIgnore
    private LocalDateTime createDate;

    @Column(name = "modify_date", nullable = false)
    @UpdateTimestamp
    @JsonIgnore
    private LocalDateTime modifyDate;

}
