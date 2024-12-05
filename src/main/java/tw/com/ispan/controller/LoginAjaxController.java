package tw.com.ispan.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import tw.com.ispan.domain.MemberBean;
import tw.com.ispan.jwt.JsonWebTokenUtility;
import tw.com.ispan.service.MemberService;
import tw.com.ispan.service.EmailService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/ajax/secure")
@CrossOrigin
public class LoginAjaxController {
    @Autowired
    private JsonWebTokenUtility jsonWebTokenUtility;

    @Autowired
    private MemberService memberService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/login")
    public String login(@RequestBody String entity) {

        // PARSE 資料
        JSONObject obj = new JSONObject(entity);
        String account = obj.optString("username", null);
        String password = obj.optString("password", null);

        // 驗證資料
        if (account == null || password == null || account.isEmpty() || password.isEmpty()) {
            return new JSONObject().put("success", false).put("message", "請輸入帳號/密碼").toString();
        }

        MemberBean bean = memberService.login(account, password);
        if (bean == null) {
            return new JSONObject().put("success", false).put("message", "登入失敗").toString();
        }

        JSONObject responseJson = new JSONObject()
                .put("success", true)
                .put("message", "登入成功")
                .put("account", bean.getAccount())
                .put("email", bean.getEmail())
                .put("phone", bean.getPhoneNo())
                .put("birthDate", bean.getBirthDate());
        return responseJson.toString();

    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody String entity) {
        JSONObject obj = new JSONObject(entity);
        String username = obj.optString("username");

        // Validate the input
        if (username == null || username.isEmpty()) {
            return new JSONObject()
                    .put("success", false)
                    .put("message", "請輸入有效的使用者帳號")
                    .toString();
        }

        // Retrieve the email based on the username
        MemberBean member = memberService.findByAccount(username);
        if (member == null) {
            return new JSONObject()
                    .put("success", false)
                    .put("message", "找不到該使用者")
                    .toString();
        }

        // Generate a secure token for password reset
        String resetToken = jsonWebTokenUtility.createToken(member.getEmail());
        emailService.sendResetPasswordEmail(member.getEmail(), resetToken);

        return new JSONObject()
                .put("success", true)
                .put("message", "重設密碼的郵件已發送")
                .toString();
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody String entity) {
        JSONObject obj = new JSONObject(entity);
        String token = obj.optString("token");
        String newPassword = obj.optString("password");

        // Validate token
        String email = jsonWebTokenUtility.parseToken(token);
        if (email == null || newPassword == null || newPassword.isEmpty()) {
            return new JSONObject()
                    .put("success", false)
                    .put("message", "無效的重設請求")
                    .toString();
        }

        // Update password in the database
        memberService.updatePassword(email, newPassword);

        return new JSONObject()
                .put("success", true)
                .put("message", "密碼重設成功")
                .toString();
    }

}
