package cl.proyecto.Factura.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filtroPro(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/facturas/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/facturas/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/facturas/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/facturas/**").authenticated()
                )
                .httpBasic(httpBasic -> {});
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsManager(PasswordEncoder encoder) {
        UserDetails user = User.builder()
                .username("Maty")
                .password(encoder.encode("DuocUC..2026"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}