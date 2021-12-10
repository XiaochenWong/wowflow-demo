package top.withwings.wowflow.demo.city.library.membership;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.withwings.wow.flow.model.Process;
import top.withwings.wow.flow.service.PersistenceProvider;

@Service
public class ApplicationPersistenceProvider implements PersistenceProvider<String, String> {


    @Autowired
    private ProcessRepository processRepository;

    @Override
    public void persist(Process<String, String> process) {
        processRepository.save(ProcessRecord.from(process));
    }

    @Override
    public Process<String, String> get(String id) {
        return processRepository.findById(id).get().toProcess();
    }
}
