package org.uchicago.regie.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.uchicago.regie.model.StudentEntity;

import java.util.List;

public interface StudentRepository extends CrudRepository<StudentEntity, Long> {

    StudentEntity getStudentByEmail(String email);

    List<StudentEntity> findByEnabled(boolean enabled);

    boolean existsByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE students SET enabled = :enabled WHERE email = :email")
    void updateStudentEnabledStatus(@Param("email") String email, @Param("enabled") boolean enabled);

    @Modifying
    @Transactional
    @Query("UPDATE students SET password = :password WHERE email = :email")
    void updateStudentPassword(@Param("email") String email, @Param("password") String password);

}
