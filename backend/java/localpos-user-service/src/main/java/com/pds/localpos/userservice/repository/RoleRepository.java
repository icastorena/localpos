package com.pds.localpos.userservice.repository;

import com.pds.localpos.userservice.model.Role;
import com.pds.localpos.userservice.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    Set<Role> findByNameIn(Set<RoleName> names);
}
