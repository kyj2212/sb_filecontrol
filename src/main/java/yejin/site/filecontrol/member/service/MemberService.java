package yejin.site.filecontrol.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Authenticator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import yejin.site.filecontrol.base.exception.SignupEmailDuplicatedException;
import yejin.site.filecontrol.base.exception.SignupUsernameDuplicatedException;
import yejin.site.filecontrol.member.entity.Member;
import yejin.site.filecontrol.member.repository.MemberRepository;
import yejin.site.filecontrol.util.Util;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    @Value("${custom.genFileDirPath}")
    private String uploadDir;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;



    public Member create(String username, String password, String name, String email, MultipartFile profileImg) throws SignupUsernameDuplicatedException, SignupEmailDuplicatedException {
        log.debug("profileimg : " +profileImg.getContentType() );

        String ext = Util.file.getExt(profileImg.getOriginalFilename());
        String profileImgDirName = getCurrentProfileImgDirName();
        String profileImgFileName = UUID.randomUUID().toString() + "." + ext;

        String profileImgDirPath = uploadDir + "/" + profileImgDirName;
        String profileImgFilePath = profileImgDirPath + "/" +  profileImgFileName;


        File profileImgFile = new File(profileImgFilePath);
        log.debug("profileImgFile : "+ profileImgFile);

        if(new File(profileImgDirPath).mkdirs()){
            log.debug("디렉토리 생성 실패");
        }

        try{
            profileImg.transferTo(profileImgFile);
        }catch (IOException e){
            log.debug("파일 변환 실패");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        String profileImgRealPath = profileImgDirName + "/" + profileImgFileName;
        Member member = new Member(username, passwordEncoder.encode(password), name, email,profileImgRealPath);

        try {
            memberRepository.save(member);
        } catch (DataIntegrityViolationException e) {

            if (memberRepository.existsByUsername(username)) {
                throw new SignupUsernameDuplicatedException("중복된 ID 입니다.");
            } else {
                throw new SignupEmailDuplicatedException("중복된 이메일 입니다.");
            }
        }
        return member;
    }

    public Member create(String username, String password, String name, String email) throws SignupUsernameDuplicatedException, SignupEmailDuplicatedException {

        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .name(name)
                .email(email)
                .build();

        try {
            memberRepository.save(member);
        } catch (DataIntegrityViolationException e) {

            if (memberRepository.existsByUsername(username)) {
                throw new SignupUsernameDuplicatedException("중복된 ID 입니다.");
            } else {
                throw new SignupEmailDuplicatedException("중복된 이메일 입니다.");
            }
        }
        return member;
    }

    private String getCurrentProfileImgDirName() {
        return "member/" + Util.date.getCurrentDateFormatted("yyyy_MM_dd");
    }

    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }
    public Member findById(long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 사용자 입니다."));
    }

    @Transactional
    public void delete(long id) {
        memberRepository.delete(findById(id));
    }

    public void update(Member orgMember, Member newMember) {
        orgMember.updateName(newMember.getName());
        orgMember.updateEmail(newMember.getEmail());
        orgMember.setEncryptedPassword(newMember.getPassword());
    }

    public void login(String username, String password) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    public long count() {
        return memberRepository.count();
    }

    public void removeProfileImg(Member member) {
        member.removeProfileImgOnStorage();
        member.setProfileImg(null); // 일부러 null을 넣는 이유를 알고싶다. 왜 이미지를 지울까?
        memberRepository.save(member);
    }
    public void setProfileImgByUrl(Member member, String url){

    }
}
