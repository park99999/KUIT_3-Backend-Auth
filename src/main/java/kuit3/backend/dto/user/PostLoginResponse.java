package kuit3.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostLoginResponse {

    private long userId;
    private String jwt;

}