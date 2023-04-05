package uz.pdp.lesson51hr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.lesson51hr.entity.enums.TaskProcessName;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TaskProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private TaskProcessName taskProcessName;

}
