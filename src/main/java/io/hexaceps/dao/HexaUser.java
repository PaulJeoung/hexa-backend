package io.hexaceps.dao;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter
@ToString(exclude = "userRoleList")
public class HexaUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    // private String userId = UUID.randomUUID().toString();

    private String email;

    private String username;

    private String nickname;

    private String password;

    @Column(length = 13)
    private String phoneNumber;

    @Column(length = 100)
    private String address;

    private String size;

    private LocalDate registeredAt;

    // @ColumnDefault("TRUE")
    private boolean newsletter;

    private String favourite;

    @Column(length = 5)
    // @ColumnDefault("11111")
    private int agree;

    @Column(name = "social_login")
    private boolean socialLogin;

    @Column(length = 4)
    // @ColumnDefault("0000")
    private int activate;

    @Column(length = 2)
    // @ColumnDefault("01")
    private char rank;

    // userRoleList 가 사용 될때 확인할 데이터를 로딩
    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private List<UserRole> userRoleList = new ArrayList<>();

    public void addRole(UserRole userRole) { // role 부여
        userRoleList.add(userRole);
    }
    public void clearRole(UserRole userRole) { // role 제거
        userRoleList.clear();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setNewsletter(boolean newsletter) {
        this.newsletter = newsletter;
    }

    public void setSocialLogin(boolean socialLogin) {
        this.socialLogin = socialLogin;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setFavourite(String favourite) {
        this.favourite = favourite;
    }

}
