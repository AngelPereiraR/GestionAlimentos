package daw2a.gestionalimentos.security;

import daw2a.gestionalimentos.security.jwt.JwtRequestFilter;
import daw2a.gestionalimentos.security.user.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Deshabilitar CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/authenticate", "/auth/register", "/swagger-ui.html", "/swagger-ui/**", "/v3/**", "/swagger-resources/**", "/favicon.ico", "/error").permitAll()
                        .requestMatchers("/api/alimentos/**").hasAnyRole("USUARIO", "ADMIN")
                        .requestMatchers("/api/almacenes/**").hasAnyRole("ADMIN", "USUARIO")
                        .requestMatchers("/api/recipientes/**").hasAnyRole("ADMIN", "USUARIO")
                        .requestMatchers("/api/secciones/**").hasAnyRole("ADMIN", "USUARIO")
                        .requestMatchers("/usuarios/vista").hasRole("ADMIN")
                        .anyRequest().authenticated() // Requiere autenticación para otras rutas
                )
                .httpBasic(Customizer.withDefaults()) // Habilitar Basic Authentication
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless para JWT
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // Agregar filtro JWT

        return http.build();
    }
}
