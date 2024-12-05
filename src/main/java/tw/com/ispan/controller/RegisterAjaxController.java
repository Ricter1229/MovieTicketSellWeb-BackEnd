package tw.com.ispan.controller;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.com.ispan.domain.MemberBean;
import tw.com.ispan.service.EmailService;
import tw.com.ispan.service.MemberService;

@RestController
@RequestMapping("/ajax/secure")
@CrossOrigin
public class RegisterAjaxController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody MemberBean newMember) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 呼叫 service
            String resultMessage = memberService.registerTemp(newMember);
            System.out.println(resultMessage);
            if ("Registration successful!".equals(resultMessage)) {
                response.put("success", true);
                response.put("message", "註冊成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", resultMessage);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/register-temp")
    public String registerTemp(@RequestBody MemberBean newMember) {
        String resultMessage = memberService.registerTemp(newMember);
        if (!"Registration successful!".equals(resultMessage)) {
            return new JSONObject().put("success", false).put("message", resultMessage).toString();
        }

        String validationCode = String.format("%04d", new java.util.Random().nextInt(9999));

        Map<String, MemberBean> session = new HashMap<>();
        session.put(newMember.getEmail(), newMember); // Store in session or cache (e.g., Redis)
        emailService.sendValidationCode(newMember.getEmail(), validationCode);

        JSONObject responseJson = new JSONObject()
                .put("success", true)
                .put("validationCode", validationCode);
        System.out.println(responseJson.toString());
        return responseJson.toString();
    }

    @PostMapping("/confirm-registration")
    public String confirmRegistration(@RequestBody MemberBean newMember) {
        String resultMessage = memberService.register(newMember);
        return resultMessage;
    }

}
