package com.library.managementt.repository;
import com.library.managementt.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
public interface StudentRepository extends JpaRepository<Student,Long> {
 boolean existsByStudentId(String v); boolean existsByEmail(String v); boolean existsByStudentIdAndIdNot(String v,Long id); boolean existsByEmailAndIdNot(String v,Long id);
 @Query("select s from Student s where lower(s.name) like lower(concat('%',:q,'%')) or lower(s.studentId) like lower(concat('%',:q,'%')) or lower(s.email) like lower(concat('%',:q,'%'))") List<Student> search(@Param("q") String q);
}
