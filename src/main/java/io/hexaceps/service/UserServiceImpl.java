package io.hexaceps.service;

import io.hexaceps.dao.HexaUser;
import io.hexaceps.dao.UserRole;
import io.hexaceps.dto.UserDTO;
import io.hexaceps.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

//    public UserServiceImpl(UserRepository userRepository) { // 생성자를 @RequiredArgsConstructor 로 대체
//        this.userRepository = userRepository;
//    }

    // 전체 회원 리스트 조회
    @Override
    public List<UserDTO> getAllUsers() {
        List<HexaUser> users = userRepository.findAll();
        return users.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // 회원 이메일 조회
    @Override
    public UserDTO getUserByEmail(String email) {
        HexaUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with email " + email + " not found"));
        return mapToDTO(user);
    }

    // 신규 회원 생성, 가입
    @Override
    public UserDTO createUser(UserDTO userDTO) {
        HexaUser user = mapToEntity(userDTO);
        HexaUser createdUser = userRepository.save(user);
        return mapToDTO(createdUser);
    }

    // 회원 정보 수정, 업데이트
    @Override
    public UserDTO updateUser(String email, UserDTO userDTO) {
        HexaUser existUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with email " + email + " not found"));

        existUser.setUsername(userDTO.getUsername());
        existUser.setPassword(userDTO.getPassword());
        existUser.setAddress(userDTO.getAddress());
        existUser.setPhoneNumber(String.valueOf(userDTO.getPhoneNumber()));
        existUser.setNewsletter(userDTO.isNewsletter());
        // existUser.setSocialLogin(userDTO.isSocialLogin()); 사용안함 (변동 없는 정책)

        // 역할 업데이트
        existUser.getUserRoleList().clear();
        userDTO.getRoleNames().forEach(role -> existUser.addRole(UserRole.valueOf(role)));

        HexaUser updatedUser = userRepository.save(existUser);
        return mapToDTO(updatedUser);
    }

    // 회원 정보 삭제 (구현만 하고, 실제 사용 X)
    @Override
    public Map<String, String> deleteUser(String email) {
        HexaUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with email " + email + " not found"));
        userRepository.delete(user);
        return Map.of("DELETE EMAIL INFO", email);
    }

    // Entity 정보를  DTO로 변환 해서 리턴 (조회를 위해서)
    private UserDTO mapToDTO(HexaUser user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setAddress(user.getAddress());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setNewsletter(user.isNewsletter());
        userDTO.setSocialLogin(user.isSocialLogin());
        userDTO.setRoleNames(user.getUserRoleList().stream().map(UserRole::name).collect(Collectors.toList()));
        return userDTO;
    }

    // DTO 정보를 Entity builder 패턴 적용 후 리턴 (저장, 업데이트를 위해서)
    private HexaUser mapToEntity(UserDTO userDTO) {
        HexaUser user = HexaUser.builder()
                .email(userDTO.getEmail())
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .phoneNumber(userDTO.getPhoneNumber())
                .newsletter(userDTO.isNewsletter())
                .socialLogin(userDTO.isSocialLogin())
                .build();
        userDTO.getRoleNames().forEach(role -> user.addRole(UserRole.valueOf(role)));
        return user;
    }
}
