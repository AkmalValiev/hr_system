package uz.pdp.lesson51hr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.lesson51hr.entity.TaskProcess;
import uz.pdp.lesson51hr.entity.enums.TaskProcessName;

public interface TaskProcessRepository extends JpaRepository<TaskProcess, Integer> {

    TaskProcess findByTaskProcessName(TaskProcessName taskProcessName);

}
