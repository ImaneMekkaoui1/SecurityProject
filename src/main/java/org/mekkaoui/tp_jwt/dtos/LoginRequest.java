package org.mekkaoui.tp_jwt.dtos;

public record LoginRequest(
        String email,
        String password
) {
}
