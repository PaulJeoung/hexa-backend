package io.hexaceps.service;

import io.hexaceps.dto.UserDTO;

import java.util.List;
import java.util.Map;

public interface UserService {

    // 모든 사용자 조회
    List<UserDTO> getAllUsers();

    // 이메일로 사용자 조회
    UserDTO getUserByEmail(String email);

    // 사용자 생성
    UserDTO createUser(UserDTO userDTO);

    // 사용자 정보 업데이트
    UserDTO updateUser(String email, UserDTO userDTO);

    // 사용자 정보 삭제
    Map<String, String> deleteUser(String email);

}
