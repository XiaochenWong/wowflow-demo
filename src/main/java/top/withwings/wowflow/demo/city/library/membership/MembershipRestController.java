package top.withwings.wowflow.demo.city.library.membership;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.withwings.wow.flow.model.Log;
import top.withwings.wow.flow.model.Process;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class MembershipRestController {

    @Autowired
    private MembershipService membershipService;


    @PostMapping("/apply")
    public String apply(@RequestBody MembershipApplication application) {
        String id = UUID.randomUUID().toString();
        application.setApplicationId(id);
        membershipService.startApplication(application);
        return "申请已提交, 流水号:" + id;
    }


    @GetMapping("/apply/{id}")
    public Object info(@PathVariable("id") String id) {
        Process<String, String> process = membershipService.getById(id);

        MembershipApplication application = membershipService.findApplication(id);

        String timeline = process.logs().stream().map(Log::toString).collect(Collectors.joining("\n"));

        return Map.of("info", application, "current", process.current().getTitle(), "timeline", timeline);
    }

    @PostMapping("/apply/{id}/socialCheck")
    public String socialCheck(@PathVariable("id") String id, Boolean passed, String comment) {

        MembershipApplication application = membershipService.findApplication(id);
        try {
            membershipService.checkSocialNumber(application, passed, comment);
            return "ok";
        } catch (IllegalStateException e) {
            return e.getMessage();
        }
    }

    @PostMapping("/apply/{id}/backgroundSurvey")
    public String backgroundSurvey(@PathVariable("id") String id, Boolean passed, String comment) {

        MembershipApplication application = membershipService.findApplication(id);
        try {
            membershipService.surveyBackground(application, passed, comment);
            return "ok";
        } catch (IllegalStateException e) {
            return e.getMessage();
        }
    }
}
