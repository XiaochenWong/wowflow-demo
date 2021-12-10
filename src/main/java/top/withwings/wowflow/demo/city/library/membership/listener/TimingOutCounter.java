package top.withwings.wowflow.demo.city.library.membership.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.withwings.wow.flow.model.Process;
import top.withwings.wow.flow.service.ChangeListener;
import top.withwings.wowflow.demo.city.library.membership.MembershipApplication;
import top.withwings.wowflow.demo.city.library.membership.MembershipService;
import top.withwings.wowflow.demo.city.library.membership.TimeCountingRecord;
import top.withwings.wowflow.demo.city.library.membership.TimeCountingRepository;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Component
public class TimingOutCounter implements ChangeListener<String> {

    MembershipService membershipService;

    TimeCountingRepository timeCountingRepository;

    @Autowired
    public void setMembershipService(@Lazy MembershipService membershipService,
                                     TimeCountingRepository timeCountingRepository) {
        this.membershipService = membershipService;
        this.timeCountingRepository = timeCountingRepository;
    }

    @Override
    public void onChange(String id) {
        final Process<String, String> process = membershipService.getById(id);
        if (process.finished()) {
            timeCountingRepository.deleteById(id);
        }
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void timeOutDetect() {
        System.out.println("checking time out");
        timeCountingRepository.findAll().forEach(this::timeOutDetect);
    }

    private void timeOutDetect(TimeCountingRecord timeCountingRecord) {
        if (timeCountingRecord.getTimeOutAt().isBefore(LocalDateTime.now())) {

            Process<String, String> process = membershipService.getById(timeCountingRecord.getId());

            if (!process.finished()) {
                process.handle(MembershipApplication.EVENT_TIMEOUT, "申请超时");
            }

            timeCountingRepository.delete(timeCountingRecord);
        }
    }


    public void startCounting(String applicationId) {
        TimeCountingRecord timeCountingRecord = new TimeCountingRecord();
        timeCountingRecord.setCreatedAt(LocalDateTime.now());
        timeCountingRecord.setTimeOutAt(LocalDateTime.now().plusMinutes(5));
        timeCountingRecord.setId(applicationId);
        timeCountingRepository.save(timeCountingRecord);
    }
}
