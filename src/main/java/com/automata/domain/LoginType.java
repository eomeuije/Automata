package com.automata.domain;

public enum LoginType {
    FORM {
        @Override
        public String getMessage() {
            return "아이디/비밀번호";
        }
    },
    GOOGLE {
        @Override
        public String getMessage() {
            return "Google";
        }
    };

    public abstract String getMessage();
}
