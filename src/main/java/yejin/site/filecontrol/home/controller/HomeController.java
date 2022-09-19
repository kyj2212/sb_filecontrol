package yejin.site.filecontrol.home.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import yejin.site.filecontrol.member.entity.Member;
import yejin.site.filecontrol.member.service.MemberService;

import java.io.File;
import java.io.IOException;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {
    private final MemberService memberService;

    @RequestMapping("/")
    public String main(Principal principal, Model model){
        Member member = memberService.findByUsername(principal.getName());
        model.addAttribute("member",member);
        return "home/main";
    }
    @RequestMapping("/test/upload")
    public String upload(){
        return "home/test/upload";
    }

}
