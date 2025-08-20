package com.gonaeun.springjwtproject.domain;

import lombok.*;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class User {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private Role role;
}