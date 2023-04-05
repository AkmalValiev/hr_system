package uz.pdp.lesson51hr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.lesson51hr.entity.Task;
import uz.pdp.lesson51hr.entity.TaskProcess;
import uz.pdp.lesson51hr.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    Optional<Task> findByUserIdAndId(UUID user_id, Integer id);

}
