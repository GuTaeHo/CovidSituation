package com.cosiguk.covidsituation.util;

import java.util.regex.Pattern;

public class PatternUtil {
    // 닉네임 패턴 (영어, 한국어, 숫자, 최대 10자리 까지)
    private static final Pattern nickPattern = Pattern.compile("^[가-힣ㄱ-ㅎa-zA-Z0-9._-]{3,10}$");
    // 비밀번호 패턴 (숫자, 문자, 비밀번호 최소 5자에서 최대 20자까지 허용)
    private static final Pattern passwordPattern = Pattern.compile("^[가-힣ㄱ-ㅎa-zA-Z0-9._-]{5,20}$");
    // private static final Pattern passwordPattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d~!@#$%^&*()+|=]{5,20}$");

    public static boolean isNickNamePattern(String nickName) {
        return !nickPattern.matcher(nickName).find();
    }
    public static boolean isPassWordPattern(String password) {
        return !passwordPattern.matcher(password).find();
    }
}
