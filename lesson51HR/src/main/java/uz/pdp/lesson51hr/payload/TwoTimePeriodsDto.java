package uz.pdp.lesson51hr.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TwoTimePeriodsDto {

    private String firstTime;
    private String secondTime;

}
