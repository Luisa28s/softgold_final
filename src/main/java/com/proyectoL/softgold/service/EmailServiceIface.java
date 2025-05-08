package com.proyectoL.softgold.service;

public interface EmailServiceIface {
    void sendPasswordResetEmail(String email, String resetLink);
}
