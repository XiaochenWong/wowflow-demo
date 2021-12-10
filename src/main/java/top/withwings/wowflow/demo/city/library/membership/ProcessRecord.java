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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
@Document("process")
@NoArgsConstructor
@AllArgsConstructor
public class ProcessRecord {

    @Id
    private String id;
    private String currentStepKey;
    private Map<String, Step> steps;
    private List<Log<String>> logs;


    public Process<String, String> toProcess() {
        return Process.historicalInstance(id, steps, currentStepKey, logs);
    }

    public static ProcessRecord from(Process<String, String> source) {

        String currentKey = source.steps().keySet().stream().filter(k -> source.steps().get(k)
                == source.current()).findAny().get();
        return new ProcessRecord(source.getId(), currentKey, source.steps(), source.logs());
    }

}
