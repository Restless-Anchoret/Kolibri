package com.ran.kolibri.service

import com.ran.kolibri.entity.comment.Comment
import com.ran.kolibri.entity.comment.CommentsHolder
import com.ran.kolibri.exception.BadRequestException
import com.ran.kolibri.exception.ForbiddenException
import com.ran.kolibri.extension.logInfo
import com.ran.kolibri.repository.comment.CommentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentService {

    @Autowired
    lateinit var commentRepository: CommentRepository

    @Autowired
    lateinit var userService: UserService

    @Transactional
    fun <T : CommentsHolder> addComment(commentsHolder: T,
                                        holderRepository: CrudRepository<T, Long>,
                                        text: String) {
        logInfo { "Adding a comment: text = $text" }
        if (text.trim().isEmpty()) {
            logInfo { "Text is empty, comment was not added" }
            return
        }

        val comment = Comment(text)
        comment.author = userService.getAuthenticatedUser()
        commentRepository.save(comment)

        commentsHolder.comments.add(comment)
        holderRepository.save(commentsHolder)
        logInfo { "Comment was added" }
    }

    @Transactional
    fun <T : CommentsHolder> editComment(commentsHolder: T,
                                         commentIndex: Int,
                                         text: String) {
        logInfo { "Editing a comment: commentIndex = $commentIndex, text = $text" }
        if (text.trim().isEmpty()) {
            logInfo { "Text is empty, comment was not edited" }
            return
        }

        if (commentIndex !in 0..(commentsHolder.comments.size - 1)) {
            throw BadRequestException("Comment does not exist")
        }

        val comment = commentsHolder.comments[commentIndex]
        if (comment.author?.id != userService.getAuthenticatedUserData().id) {
            throw ForbiddenException("Forbidden to edit a comment of another User")
        }

        comment.text = text
        commentRepository.save(comment)
        logInfo { "Comment was edited" }
    }

    @Transactional
    fun <T : CommentsHolder> removeComment(commentsHolder: T,
                                           holderRepository: CrudRepository<T, Long>,
                                           commentIndex: Int) {
        logInfo { "Removing a comment: commentIndex = $commentIndex" }
        if (commentIndex !in 0..(commentsHolder.comments.size - 1)) {
            throw BadRequestException("Comment does not exist")
        }

        val comment = commentsHolder.comments[commentIndex]
        if (comment.author?.id != userService.getAuthenticatedUserData().id) {
            throw ForbiddenException("Forbidden to remove a comment of another User")
        }

        val oldComments = commentsHolder.comments.map { it }
        commentRepository.delete(oldComments)

        commentsHolder.comments.clear()
        oldComments.forEachIndexed { index, comment ->
            if (index != commentIndex) {
                val newComment = comment.clone()
                commentRepository.save(newComment)
                commentsHolder.comments.add(newComment)
            }
        }
        holderRepository.save(commentsHolder)
        logInfo { "Comment was removed" }
    }

    @Transactional
    fun <T : CommentsHolder> cloneComments(commentsHolderFrom: T,
                                           commentsHolderTo: T,
                                           holderRepository: CrudRepository<T, Long>) {
        commentsHolderFrom.comments.forEach { comment ->
            val newComment = comment.clone()
            newComment.author = comment.author
            commentRepository.save(newComment)
            commentsHolderTo.comments.add(newComment)
        }
        holderRepository.save(commentsHolderTo)
    }

}
