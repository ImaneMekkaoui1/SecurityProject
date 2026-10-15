package org.mekkaoui.tp_jwt.dtos;


public record RegisterRequest(
        String email,
        String password
) {
}
