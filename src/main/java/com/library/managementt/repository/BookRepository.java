package com.library.managementt.repository;
import com.library.managementt.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
public interface BookRepository extends JpaRepository<Book,Long> {
 @Override @Lock(LockModeType.PESSIMISTIC_WRITE) Optional<Book> findById(Long id);
 boolean existsByIsbn(String isbn); boolean existsByIsbnAndIdNot(String isbn,Long id);
 @Query("select b from Book b where lower(b.title) like lower(concat('%',:q,'%')) or lower(b.author) like lower(concat('%',:q,'%')) or lower(b.isbn) like lower(concat('%',:q,'%')) or lower(b.category) like lower(concat('%',:q,'%'))") List<Book> search(@Param("q") String q);
}
