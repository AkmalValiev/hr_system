package uz.pdp.lesson51hr.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.cache.internal.TimestampsCacheEnabledImpl;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComeToWorkDto {

    private Timestamp timestamp;
    private String username;
}
