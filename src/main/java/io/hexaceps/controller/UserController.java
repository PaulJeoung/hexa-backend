package io.hexaceps.controller;

import io.hexaceps.dto.UserDTO;
import io.hexaceps.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    // 전체 회원 조회
    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // 회원 이메일 조회 (@PathVariable 대신 @RequestParam 을 이용해서 쿼리 매개 변수로 받게끔 구현)
    @GetMapping
    public ResponseEntity<UserDTO> getUser(@RequestParam String email) {
        log.info("Get user by email : {}", email);
        UserDTO user = userService.getUserByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // 회원 가입
    @PostMapping("/")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createUser = userService.createUser(userDTO);
        return new ResponseEntity<>(createUser, HttpStatus.CREATED);
    }

    // 회원 정보 수정, 업데이트
    @PutMapping("/{email}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String email, @RequestBody UserDTO userDTO) {
        UserDTO user = userService.updateUser(email, userDTO);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // 회원 정보 삭제 (실제 사용 하지 않을 예정)
    @DeleteMapping("/{email}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
