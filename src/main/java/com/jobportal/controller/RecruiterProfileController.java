package com.jobportal.controller;

import com.jobportal.entity.RecruiterProfile;
import com.jobportal.entity.Users;
import com.jobportal.repository.UsersRepository;
import com.jobportal.services.RecruiterProfileService;
import com.jobportal.util.FileUploadUtil;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/recruiter-profile")
public class RecruiterProfileController {
    private final UsersRepository usersRepository;
    private final RecruiterProfileService recruiterProfileService;

    public RecruiterProfileController(UsersRepository usersRepository, RecruiterProfileService recruiterProfileService) {
        this.usersRepository = usersRepository;
        this.recruiterProfileService = recruiterProfileService;
    }

    @GetMapping("/")
    public String recruiterProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            Users users = usersRepository.findByEmail(currentUsername).orElseThrow(() -> new UsernameNotFoundException("Couldn't find user"));
            Optional<RecruiterProfile> recruiterProfile = recruiterProfileService.getOne(users.getUserId());
            if (!recruiterProfile.isEmpty()) {
                model.addAttribute("profile", recruiterProfile.get());
            }
        }
        return "recruiter_profile";
    }

    @PostMapping("/addNew")
    public String addNew(RecruiterProfile recruiterProfile,
                         @RequestParam("image") MultipartFile multipartFile,
                         Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            Users users = usersRepository.findByEmail(currentUserName).orElseThrow(() -> new UsernameNotFoundException("Couldn't find user with an associated name"));
            recruiterProfile.setUserId(users);
            recruiterProfile.setUserAccountId(users.getUserId());
        }
        model.addAttribute("profile", recruiterProfile);

        String fileName = "";
        if (!Objects.equals(multipartFile.getOriginalFilename(), "")) {
            fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename())); //Cleans the filename prevent security issues
            recruiterProfile.setProfilePhoto(fileName); //Setting up profile photo as String
        }
        RecruiterProfile savedUser = recruiterProfileService.addNew(recruiterProfile);
        String uploadDir = "photos/recruiter/" + savedUser.getUserAccountId();

        try {
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile); //Saving file into our system
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/dashboard";
    }
}
