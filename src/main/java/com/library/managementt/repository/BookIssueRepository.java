package com.library.managementt.repository;
import com.library.managementt.entity.BookIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
public interface BookIssueRepository extends JpaRepository<BookIssue,Long> {
 @Override @Lock(LockModeType.PESSIMISTIC_WRITE) Optional<BookIssue> findById(Long id);
 long countByStatus(String status); boolean existsByBookIdAndStatus(Long id,String status); boolean existsByStudentIdAndStatus(Long id,String status); List<BookIssue> findByStatusOrderByIssueDateDesc(String status);
 boolean existsByBook_IdAndStudent_IdAndStatus(Long bookId,Long studentId,String status);
 @Query("select i from BookIssue i where (:status is null or i.status=:status) and (:q is null or lower(i.book.title) like lower(concat('%',:q,'%')) or lower(i.student.name) like lower(concat('%',:q,'%')) or lower(i.student.studentId) like lower(concat('%',:q,'%')))") List<BookIssue> search(@Param("q") String q,@Param("status") String status);
}
