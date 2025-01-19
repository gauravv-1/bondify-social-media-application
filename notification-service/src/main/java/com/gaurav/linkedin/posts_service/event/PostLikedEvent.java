package com.gaurav.linkedin.posts_service.event;

import lombok.Data;

@Data
public class PostLikedEvent {
    Long postId;
    Long creatorId;
    Long likedByUserId;
    String likedByUserName;
    String userProfileUrl;
}
