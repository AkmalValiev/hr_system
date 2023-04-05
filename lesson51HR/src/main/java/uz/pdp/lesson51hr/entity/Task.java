package uz.pdp.lesson51hr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String description;
    private LocalDate expireDateOfTask;
    @ManyToOne
    private User user;
    @ManyToOne
    private User fromUser;
    private boolean done;

    @ManyToOne
    private TaskProcess taskProcess;

}
