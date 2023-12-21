package ru.nikitapopov.weathermeasuresapi.utils;

public enum Authority {

    ROLE_USER("ROLE_USER"), ROLE_ADMIN("ROLE_ADMIN"), ROLE_EMPLOYEE("ROLE_EMPLOYEE");

    private final String role;

    Authority(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return this.role;
    }
}
