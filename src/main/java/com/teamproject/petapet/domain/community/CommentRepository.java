package com.teamproject.petapet.domain.community;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByCommunityCommunityId(Long communityId, Pageable pageable);

    int countCommentByCommunityCommunityId(Long communityId);

    @Modifying
    @Transactional
    @Query("update Comment c set c.replyId =:commentId where c.commentId =:commentId")
    void updateReplyId(Long commentId);

    Comment findFirstByReplyIdOrderByCommentIdAsc(Long replyId);

//    @Query(value = "SELECT IFNULL(b.result,0) AS isDeleted, a.* " +
//            "FROM Comment a " +
//                "left JOIN " +
//                "(SELECT 1 AS result, a.replyId, min(a.createdDate) AS minOrder " +
//                "from (SELECT if(result = 1, result, 0) AS result, b.* " +
//                    "from (SELECT replyId, 1 AS result " +
//                        "from Comment " +
//                        "WHERE communityId =:communityId " +
//                        "GROUP BY replyId " +
//                        "having avg(depth) = 1) a " +
//                        "right JOIN Comment b ON b.replyId = a.replyId) a " +
//                "WHERE a.result = 1 " +
//                "GROUP BY a.replyId) b " +
//                "ON a.replyId = b.replyId AND a.createdDate = b.minOrder " +
//            "WHERE communityId =:communityId "
//            ,countQuery = "select count(*) from Comment where communityId =:communityId"
//            ,nativeQuery = true)
//    Page<Comment> test(Long communityId, Pageable pageable);


    @Query(value = "SELECT IFNULL(b.result,0) AS depth, a.* " +
            "FROM Comment a " +
            "left JOIN " +
            "(SELECT 1 AS result, a.replyId, min(a.createdDate) AS minOrder " +
            "from (SELECT if(result = 1, result, 0) AS result, b.* " +
            "from (SELECT replyId, 1 AS result " +
            "from Comment " +
            "WHERE communityId =:communityId " +
            "GROUP BY replyId " +
            "having avg(depth) = 1) a " +
            "right JOIN Comment b ON b.replyId = a.replyId) a " +
            "WHERE a.result = 1 " +
            "GROUP BY a.replyId) b " +
            "ON a.replyId = b.replyId AND a.createdDate = b.minOrder " +
            "WHERE communityId =:communityId "
            ,countQuery = "select count(*) from Comment where communityId =:communityId"
            ,nativeQuery = true)
    Page<Comment> test2(Long communityId,Pageable pageable);

}
