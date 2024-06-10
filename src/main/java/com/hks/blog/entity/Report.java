package com.hks.blog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hks.blog.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Report extends BaseEntity {
    private String subject;
    @ManyToOne
    @JsonIgnore
    private User user;
}
