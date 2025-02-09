package com.example.project_orion.repository;

import com.example.project_orion.models.Question;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Question findByTitle(@NotBlank @Size(min = 3, max = 50, message = "must contain at-least 3 characters & at-max 25 characters") String title);

    @Query("SELECT MAX(q.questionId) FROM Question q")
    Long findMaxId();

    @Query(value = "SELECT * FROM questions q " +
            "WHERE (:title = '' OR q.title LIKE CONCAT('%', :title, '%')) " +
            "AND (:subject IS NULL OR q.subject = :subject) " +
            "AND (:difficulty IS NULL OR q.difficulty = :difficulty) " +
            "AND (:status IS NULL OR q.status = :status)",
            nativeQuery = true)
    List<Question> findQuestions(
            @Param("title") String title,
            @Param("subject") String subject,
            @Param("difficulty") String difficulty,
            @Param("status") String status);

    @Query(value = "SELECT DISTINCT q.* FROM questions q " +
            "JOIN question_tags qt ON q.question_id = qt.question_id " +
            "JOIN tags t ON qt.tag_id = t.tag_id " +
            "WHERE (:title = '' OR q.title LIKE CONCAT('%', :title, '%')) " +
            "AND (:subject IS NULL OR q.subject = :subject) " +
            "AND (:difficulty IS NULL OR q.difficulty = :difficulty) " +
            "AND (:tagIds IS NULL OR q.question_id IN ( " +
            "    SELECT qt_inner.question_id " +
            "    FROM question_tags qt_inner " +
            "    WHERE qt_inner.tag_id IN (:tagIds) " +
            "    GROUP BY qt_inner.question_id " +
            "    HAVING COUNT(DISTINCT qt_inner.tag_id) = :tagCount " +
            ")) " +
            "AND (:status IS NULL OR q.status = :status)",
            nativeQuery = true)
    List<Question> findQuestionsWithTags(
            @Param("title") String title,
            @Param("subject") String subject,
            @Param("difficulty") String difficulty,
            @Param("tagIds") List<Long> tagIds,
            @Param("tagCount") Integer tagCount,
            @Param("status") String status);
}