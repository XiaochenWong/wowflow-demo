package top.withwings.wowflow.demo.city.library.membership;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import top.withwings.wow.flow.model.Log;
import top.withwings.wow.flow.model.Process;
import top.withwings.wow.flow.model.Step;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Data
@Document("time_count")
@NoArgsConstructor
public class TimeCountingRecord {


    @Id
    private String id;

    private LocalDateTime createdAt;

    private LocalDateTime timeOutAt;

}
