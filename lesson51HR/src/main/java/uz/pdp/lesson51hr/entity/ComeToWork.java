package uz.pdp.lesson51hr.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ComeToWork {

    @Id
    @GeneratedValue
    private UUID id;

    private Timestamp timeToWork;

    @ManyToOne
    private User user;

}
