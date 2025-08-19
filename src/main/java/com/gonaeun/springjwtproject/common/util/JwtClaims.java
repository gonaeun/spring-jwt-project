package com.gonaeun.springjwtproject.common.util;

public final class JwtClaims {
    private JwtClaims() {}
    public static final String SUBJECT = "sub";
    public static final String ROLES   = "roles";
    public static final String ISSUED_AT = "iat";
    public static final String EXPIRES_AT = "exp";
}
