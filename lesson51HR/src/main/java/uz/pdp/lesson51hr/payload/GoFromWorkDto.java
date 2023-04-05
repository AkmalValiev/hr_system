package uz.pdp.lesson51hr.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoFromWorkDto {

    private Timestamp timestamp;
    private String username;

}
