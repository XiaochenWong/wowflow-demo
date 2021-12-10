package top.withwings.wowflow.demo.city.library.membership;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.withwings.wow.flow.dsl.Wow;
import top.withwings.wow.flow.model.Event;
import top.withwings.wow.flow.model.Process;
import top.withwings.wow.flow.service.ChangeListener;
import top.withwings.wow.flow.service.PersistenceProvider;
import top.withwings.wow.flow.service.ProcessMaster;
import top.withwings.wowflow.demo.city.library.membership.listener.ResultNotifier;
import top.withwings.wowflow.demo.city.library.membership.listener.TimingOutCounter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static top.withwings.wowflow.demo.city.library.membership.MembershipApplication.*;


@Service
public class MembershipService extends ProcessMaster<String, String> {

    @Autowired
    MembershipApplicationRepository membershipApplicationRepository;
    @Autowired
    ApplicationPersistenceProvider persistenceProvider;
    @Autowired
    ResultNotifier resultNotifier;
    @Autowired
    TimingOutCounter timingOutCounter;


    public List<MembershipApplication> listApplication(Predicate<MembershipApplication> predicate) {
        return membershipApplicationRepository.findAll().stream().filter(predicate).collect(Collectors.toList());
    }

    public MembershipApplication findApplication(String id) {
        return membershipApplicationRepository.findById(id).get();
    }

    public void startApplication(MembershipApplication application) {

        String applicationId = application.getApplicationId();
        application.setTime(LocalDateTime.now());
        register(buildProcess(applicationId));
        membershipApplicationRepository.save(application);
        timingOutCounter.startCounting(applicationId);
    }

    public void checkSocialNumber(MembershipApplication application, boolean passed, String comment) {
        Process<String, String> process = getById(application.getApplicationId());
        String eventType = passed ? EVENT_SOCIAL_CHECK_VALID : EVENT_SOCIAL_CHECK_INVALID;
        boolean accepted = process.handle(new Event<>(eventType, comment));

        if (!accepted) {
            throw new IllegalStateException("not on social check step");
        }
    }

    public void surveyBackground(MembershipApplication application, boolean passed, String comment) {
        Process<String, String> process = getById(application.getApplicationId());
        String eventType = passed ? EVENT_BACKGROUND_SURVEY_OK : EVENT_BACKGROUND_SURVEY_BAD;
        boolean accepted = process.handle(new Event<>(eventType, comment));
        if (!accepted) {
            throw new IllegalStateException("not on back ground survey step");
        }
    }

    @Override
    public List<ChangeListener<String>> getChangeListeners() {
        return List.of(this.resultNotifier, this.timingOutCounter);
    }

    @Override
    public PersistenceProvider<String, String> getPersistenceProvider() {
        return this.persistenceProvider;
    }


    private Process<String, String> buildProcess(String id) {
        return
                Wow.defineProcess()
                        .withId(id)
                        .withPayloadType(String.class)
                        .startAt(STEP_SOCIAL_NUMBER_CHECK)
                        .step(STEP_SOCIAL_NUMBER_CHECK)
                        .withTitle("社保号校验")
                        .when(EVENT_SOCIAL_CHECK_VALID).goesTo(STEP_BACKGROUND_SURVEY)
                        .when(EVENT_SOCIAL_CHECK_INVALID).goesTo(STEP_FAIL)
                        .when(EVENT_TIMEOUT).goesTo(STEP_FAIL)
                        .and()
                        .step(STEP_BACKGROUND_SURVEY)
                        .withTitle("背景调查")
                        .when(EVENT_BACKGROUND_SURVEY_OK).goesTo(STEP_SUCCESS)
                        .when(EVENT_BACKGROUND_SURVEY_BAD).goesTo(STEP_FAIL)
                        .when(EVENT_TIMEOUT).goesTo(STEP_FAIL)
                        .step(STEP_FAIL)
                        .withTitle("申请失败")
                        .step(STEP_SUCCESS)
                        .withTitle("申请成功")
                        .build();
    }
}
