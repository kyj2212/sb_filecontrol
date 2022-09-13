package yejin.site.filecontrol.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import yejin.site.filecontrol.base.exception.SignupEmailDuplicatedException;
import yejin.site.filecontrol.base.exception.SignupUsernameDuplicatedException;
import yejin.site.filecontrol.member.dto.MemberDto;
import yejin.site.filecontrol.member.entity.Member;
import yejin.site.filecontrol.member.service.MemberService;

import javax.validation.Valid;
import java.security.Principal;


@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;
    private PasswordEncoder passwordEncoder;
    @GetMapping("/signup")
    public String signup(MemberDto memberDto) {
        return "/member/signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid MemberDto memberDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/member/signup_form";
        }

        if (!memberDto.getPassword1().equals(memberDto.getPassword2())) {
            bindingResult.rejectValue("memberPwd2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "/member/signup_form";
        }

        try {
            memberService.create(memberDto.getUsername(), memberDto.getPassword1(), memberDto.getName(), memberDto.getEmail(),memberDto.getProfileImg());
        } catch (SignupUsernameDuplicatedException e) {
            e.printStackTrace();
            bindingResult.reject("signupUsernameDuplicated", e.getMessage());
            return "/member/signup_form";
        } catch (SignupEmailDuplicatedException e) {
            e.printStackTrace();
            bindingResult.reject("signupEmailDuplicated", e.getMessage());
            return "/member/signup_form";
        }
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(MemberDto memberDto) {
        return "login_form";
    }

    @GetMapping("/members")
    public String detail(Model model, Principal principal){
        Member member = memberService.findByUsername(principal.getName());
        model.addAttribute("member",member);
        return "member_detail";
    }

    @DeleteMapping("/members/{id}")
    public String delete(@PathVariable long id){
        memberService.delete(id);
        return "redirect:/logout";
    }

}