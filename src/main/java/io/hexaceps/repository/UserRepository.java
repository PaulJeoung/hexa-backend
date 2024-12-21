package io.hexaceps.repository;

import io.hexaceps.dao.HexaUser;
import io.hexaceps.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface UserRepository extends JpaRepository<HexaUser, String> {

    @EntityGraph(attributePaths = {"userRoleList"})
    @Query("select user from HexaUser user where user.email = :email")
    HexaUser getWithRole(@Param("email") String email);

    Optional<HexaUser> findByEmail(String email);

}
