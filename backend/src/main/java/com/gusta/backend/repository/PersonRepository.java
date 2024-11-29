package com.gusta.backend.repository;

import com.gusta.backend.model.Person;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    @Query("SELECT p FROM Person p ORDER BY p.id ASC")
    List<Person> findAllSortedById(Pageable pageable);
    boolean existsByEmail(String email);
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Person p WHERE p.email =:email AND id =:id ")
    boolean isUserEmailByUserId(@Param("id") long id, @Param("email") String email);
}
