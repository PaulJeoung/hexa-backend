package io.hexaceps.service;

import io.hexaceps.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest

class UserServiceImplTest {
    @Autowired private UserService userService;

    @Test
    void 전체조회() {
        List<UserDTO> userDTOList = userService.getAllUsers();

    }
}