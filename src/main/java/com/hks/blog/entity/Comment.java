package com.hks.blog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hks.blog.base.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Comment extends BaseEntity {

	private String synopsis;
	
	@ManyToOne
	@JoinColumn(name = "userId")
	@JsonIgnore
	private User user;
	@ManyToOne
	@JoinColumn(name = "postId")
	@JsonIgnore
	private Post post;
}
