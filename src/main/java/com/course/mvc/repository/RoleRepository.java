package com.course.mvc.repository;

import com.course.mvc.domain.Role;
import com.course.mvc.domain.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Admin on 03.06.2017.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("Select r from Role r where r.role=:roleEnum")
    Role findRoleByName(@Param("roleEnum") RoleEnum roleEnum);
}
