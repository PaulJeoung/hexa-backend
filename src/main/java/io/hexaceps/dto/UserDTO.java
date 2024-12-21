package io.hexaceps.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @ToString
public class UserDTO {

    private String email, username, password, address, phoneNumber, size, favorite;

    private boolean newsletter, socialLogin;

    private List<String> roleNames = new ArrayList<>();

}
