package com.gaurav.linkedin.posts_service.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostCreatedEvent {
    Long creatorId;
    String content;
    Long postId;
    String profilePicUrl;
    String creatorUserName;

}
