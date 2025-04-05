package com.jobportal.controller;

import com.jobportal.entity.Users;
import com.jobportal.services.UsersService;
import com.jobportal.util.MailUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PasswordResetController {
    private final JavaMailSender mailSender;
    private final UsersService usersService;

    @Autowired
    public PasswordResetController(JavaMailSender mailSender, UsersService usersService) {
        this.mailSender = mailSender;
        this.usersService = usersService;
    }

    @GetMapping("/forgotPassword")
    public String forgotPassword() {
        return "forgot-password";
    }

    @PostMapping("/sendRequest")
    public String sendRequest(@RequestParam("email") String email, HttpServletRequest request) {
        Users user = usersService.findByEmail(email);
        if (user != null) {
            String token = MailUtil.createToken();
            try {
                usersService.updateResetPasswordToken(token, email);
                String resetPasswordLink = MailUtil.getSiteUrl(request) + "/resetForm?token=" + token;
                MailUtil.sendEmail(mailSender, email, resetPasswordLink);
            } catch (Exception e) {
                throw new RuntimeException("Can not send an email!");
            }
            return "redirect:/login";
        }else return "redirect:/forgot-password";
    }

    @GetMapping("/resetForm")
    public String getResetForm(@Param(value = "token") String token, Model model) {
        Users user = usersService.getByResetPasswordToken(token);
        if (user == null) {
            throw new RuntimeException("User not existed");
        }
        model.addAttribute("token", token);
        return "reset-form";
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam("token") String token,
                                 @RequestParam("password") String password){
        Users user= usersService.getByResetPasswordToken(token);
        if (user == null){
            throw new RuntimeException("User not existed");
        }else usersService.updatePassword(user, password);
        return "redirect:/login";
    }
}
