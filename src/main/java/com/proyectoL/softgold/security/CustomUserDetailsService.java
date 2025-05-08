package com.proyectoL.softgold.security;

import com.proyectoL.softgold.model.Usuario;
import com.proyectoL.softgold.repository.UsuarioDAO;
import com.proyectoL.softgold.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

//Autenticador de usuarios 
@Service
public class CustomUserDetailsService implements UserDetailsService {

        @Autowired
        private UsuarioDAO usuarioDAO;

        @Autowired
        private UsuarioService usuarioService;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                System.out.println("Buscando usuario con email: " + email);

                Usuario usuario = usuarioDAO.findByEmail(email)
                                .orElseThrow(() -> {
                                        System.out.println("Usuario no encontrado con email: " + email);
                                        return new UsernameNotFoundException("Usuario no encontrado");
                                });

                if (!usuarioService.isAccountNonLocked(usuario)) {
                        System.out.println("La cuenta aún está bloqueada para: " + email);
                        throw new LockedException("La cuenta está bloqueada. Intenta más tarde.");
                }

                System.out.println("Usuario encontrado: " + usuario.getEmail());
                System.out.println("Contraseña almacenada: " + usuario.getPassword());
                System.out.println("Roles del usuario: " + usuario.getRoles());
                System.out.println("¿Coinciden?: " + passwordEncoder.matches("superSegura123", usuario.getPassword()));

                usuario.getRoles().forEach(rol -> {
                        System.out.println("Nombre del rol: [" + rol.getNombre() + "]");
                });

                return new org.springframework.security.core.userdetails.User(
                                usuario.getEmail(),
                                usuario.getPassword(),
                                usuario.getRoles().stream()
                                                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombre()))
                                                .collect(Collectors.toList()));
        }

}
