package com.hks.blog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hks.blog.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Setter
@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Post extends BaseEntity {
    private String title;
    @Column(length = 5000)
    private String synopsis;
    private String image;
    private int likeCount;
    private int viewCount;

    @ManyToOne
    @JsonIgnore
    private User user;
    @OneToMany(mappedBy = "post")
    @JsonIgnore
    private List<Comment> comments;
}
