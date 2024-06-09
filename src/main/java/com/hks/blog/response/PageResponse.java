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
public class PageResponse<T> {

    private List<T>content;
    private Long totalElement;
    private int totalPage;
    private boolean first;
    private boolean last;
}
