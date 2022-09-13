package yejin.site.filecontrol.member.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Getter
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

}