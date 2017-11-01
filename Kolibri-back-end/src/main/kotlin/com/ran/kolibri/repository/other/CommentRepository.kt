package com.ran.kolibri.repository.other

import com.ran.kolibri.entity.comment.Comment
import org.springframework.data.repository.CrudRepository

interface CommentRepository : CrudRepository<Comment, Long>
