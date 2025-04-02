package com.tigeren.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;


@Data
@MappedSuperclass
@NoArgsConstructor
public class Base {
    public Base(String id) {
        this.id = id;
    }

    @Id
    private String id;

    @CreationTimestamp
    @Column(name = "inserted_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp insertedAt;

    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp updatedAt;

    @Column(name = "deleted_at", columnDefinition = "TIMESTAMP")
    private Timestamp deletedAt;
}
