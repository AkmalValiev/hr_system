package uz.pdp.lesson51hr.payload;

import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.lesson51hr.entity.TaskProcess;
import uz.pdp.lesson51hr.entity.User;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {

    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private String expireDateOfTask;
    @NotNull
    private String userEmail;
    @NotNull
    private String fromUserEmail;

}
