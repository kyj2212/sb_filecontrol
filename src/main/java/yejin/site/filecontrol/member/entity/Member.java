package yejin.site.filecontrol.member.entity;


import lombok.*;
import yejin.site.filecontrol.base.AppConfig;

import javax.persistence.*;
import java.io.File;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "member")
public class Member {

    @Id
    @Column(name="member_uid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="member_id", nullable = false, length = 20, unique = true)
    private String username;

    @Column(name="member_pwd", nullable = false)
    private String password;

    @Column(name="member_name", nullable = false, length = 20)
    private String name;

    @Column(name="member_email", unique = true)
    private String email;

    @Column(name="member_profileImg")
    private String profileImg;

    @Builder
    public Member(String username, String password, String name, String email,String profileImg) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.profileImg=profileImg;
    }

    public void updateUsername(String username){
        this.username = username;
    }
    public void setEncryptedPassword(String encryptedPassword) {
        this.password = encryptedPassword;
    }
    public void updateName(String name){
        this.name = name;
    }
    public void updateEmail(String email){
        this.email = email;
    }
    public void updateProfileImg(String profileImg){
        this.profileImg=profileImg;
    }

    public void removeProfileImgOnStorage() {
        if (profileImg == null || profileImg.trim().length() == 0) return;
        String profileImgPath = getProfileImgPath();
        new File(profileImgPath).delete();
    }
    private String getProfileImgPath() {
        return AppConfig.GET_FILE_DIR_PATH + "/" + profileImg;
    }
}