package com.proyectoL.softgold.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import com.proyectoL.softgold.service.UsuarioServiceIface;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//Encargado de manejar los errores, como incrementar intentos fallidos, bloquear cuenta y redirigir a diferentes paginas dependiendo de la causa del fallo 
@Component
public class CustomLoginPassword extends SimpleUrlAuthenticationFailureHandler {

    private String defaultFailureUrl;

    public String getDefaultFailureUrl() {
        return defaultFailureUrl;
    }

    public void setDefaultFailureUrl(String defaultFailureUrl) {
        this.defaultFailureUrl = defaultFailureUrl;
    }

    private final UsuarioServiceIface usuarioService;

    public CustomLoginPassword(UsuarioServiceIface usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {

        String email = request.getParameter("email");

        try {
            boolean bloqueado = usuarioService.incrementarIntentosFallidos(email);

            if (bloqueado) {
                // Si está bloqueado, redirige a la página especial
                setDefaultFailureUrl("/cuenta-bloqueada");
            } else {
                // Si solo fue un intento fallido normal
                setDefaultFailureUrl("/login?error=true");
            }

        } catch (Exception e) {
            // Si el usuario no existe u otro error
            setDefaultFailureUrl("/login?error=true");
        }

        getRedirectStrategy().sendRedirect(request, response, getDefaultFailureUrl());
    }

}