package top.withwings.wowflow.demo.city.library.membership;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessRepository extends MongoRepository<ProcessRecord, String> {
}
