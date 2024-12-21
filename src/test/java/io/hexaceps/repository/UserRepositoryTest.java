package io.hexaceps.repository;

import io.hexaceps.dao.HexaUser;
import io.hexaceps.dao.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

// import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest

class UserRepositoryTest {

    @Autowired private UserRepository userRepository;
    // @Autowired private PasswordEncoder passwordEncoder;

    @Test
    void 회원가입 () {
        for (int i = 1; i < 21; i++) {
            HexaUser user = HexaUser.builder()
                    .email("test"+i+"@google.com")
                    .password("1111")
                    .username("tester" + i)
                    .phoneNumber("010-" + (4521+i) + "-45" + (12+i))
                    .nickname("tester" + i)
                    .address("서울시 종로구 덕수궁" + i + "길 " + (456+i))
                    .agree(11111)
                    .rank('A')
                    .activate(0000)
                    .newsletter(true)
                    .socialLogin(false)
                    .registeredAt(LocalDate.now())
                    .build();
            user.addRole(UserRole.USER);
            if(i>=15) user.addRole(UserRole.ADMIN);
            userRepository.save(user);
        }
    }

}