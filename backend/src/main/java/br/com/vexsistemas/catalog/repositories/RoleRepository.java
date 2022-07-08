package br.com.vexsistemas.catalog.repositories;

import br.com.vexsistemas.catalog.entities.Role;
import br.com.vexsistemas.catalog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
