package uz.pdp.lesson51hr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.lesson51hr.entity.GoFromWork;

import java.util.UUID;

public interface GoFromWorkRepository extends JpaRepository<GoFromWork, UUID> {
}
