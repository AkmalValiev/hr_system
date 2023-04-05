package uz.pdp.lesson51hr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.lesson51hr.entity.ComeToWork;

import java.util.UUID;

public interface ComeToWorkRepository extends JpaRepository<ComeToWork, UUID> {
}
