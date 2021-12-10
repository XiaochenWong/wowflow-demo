package top.withwings.wowflow.demo.city.library.membership.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import top.withwings.wow.flow.model.Process;
import top.withwings.wow.flow.service.ChangeListener;
import top.withwings.wowflow.demo.city.library.membership.MembershipApplication;
import top.withwings.wowflow.demo.city.library.membership.MembershipService;

@Component
@Lazy
public class ResultNotifier implements ChangeListener<String> {


    MembershipService membershipService;

    @Autowired
    public void setMembershipService(@Lazy MembershipService membershipService) {
        this.membershipService = membershipService;
    }

    @Override
    public void onChange(String id) {
        final Process<String, String> process = membershipService.getById(id);
        if (!process.finished()) {
            return;
        }
        final MembershipApplication application = membershipService.findApplication(id);
        System.out.println("Pretending sending email to " + application.getEmail());
        System.out.println(process.current().getTitle());
        String lastComment = getLastComment(process);
        System.out.println(lastComment);
        System.out.println("_--_--_--_--_--_--_--_--_--_--_--_--_--_--_--_--_");
    }

    private String getLastComment(Process<String, String> process) {
        final String comment = process.logs().get(process.logs().size() - 1).getEvent().getPayload();
        return comment;
    }


}
