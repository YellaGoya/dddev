package com.d103.dddev.api.request.repository;

import com.d103.dddev.api.request.collection.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends MongoRepository<Comment, String> {
    Optional<List<Comment>> findByRequestId(String requestId);
}
