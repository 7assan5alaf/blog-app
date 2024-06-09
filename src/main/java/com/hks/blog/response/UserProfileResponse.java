package com.hks.blog.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserProfileResponse {
    private String name;
    private byte[] profileImage;
    private String bio;
    private String email;
    private String phone;
    private String location;
    private String link;
    private List<PostResponse> posts;
}
