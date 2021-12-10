package top.withwings.wowflow.demo.city.library.membership;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("membership_application")
public class MembershipApplication {

    public static final String STEP_SOCIAL_NUMBER_CHECK = "SOCIAL_NUMBER_CHECK";
    public static final String STEP_BACKGROUND_SURVEY = "BACKGROUND_SURVEY";
    public static final String STEP_SUCCESS = "SUCCESS";
    public static final String STEP_FAIL = "FAIL";

    public static final String EVENT_TIMEOUT = "TIMEOUT";
    public static final String EVENT_SOCIAL_CHECK_VALID = "SOCIAL_CHECK_VALID";
    public static final String EVENT_SOCIAL_CHECK_INVALID = "SOCIAL_CHECK_INVALID";
    public static final String EVENT_BACKGROUND_SURVEY_OK = "BACKGROUND_SURVEY_OK";
    public static final String EVENT_BACKGROUND_SURVEY_BAD = "BACKGROUND_SURVEY_BAD";


    @Id
    private String applicationId;
    private String applicant;
    private String socialNumber;
    private String email;
    private LocalDateTime time;


}
