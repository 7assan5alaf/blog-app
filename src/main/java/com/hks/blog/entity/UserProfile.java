package com.hks.blog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hks.blog.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "_user_profile")
public class UserProfile extends BaseEntity {

    private String bio;
    private String image;
    private String link;
    private String address;
    @OneToOne
    @JsonIgnore
    private User user;

}
