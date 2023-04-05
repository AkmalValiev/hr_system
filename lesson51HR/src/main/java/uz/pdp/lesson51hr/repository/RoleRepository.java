package uz.pdp.lesson51hr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.lesson51hr.entity.Role;
import uz.pdp.lesson51hr.entity.enums.RoleName;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByRoleName(RoleName roleName);

}
