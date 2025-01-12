package com.example.project_orion.security;

import com.example.project_orion.security.jwt.AuthEntryPointJwt;
import com.example.project_orion.security.jwt.AuthTokenFilter;
import com.example.project_orion.security.models.AppRole;
import com.example.project_orion.security.models.Role;
import com.example.project_orion.security.models.User;
import com.example.project_orion.security.repository.RoleRepository;
import com.example.project_orion.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDate;
import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        prePostEnabled = true, // both and pre & post authorize
        securedEnabled = true, // @secure authorize
        jsr250Enabled = true // Roles Allowed
)
public class SecurityConfig {

    // 1
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                        (requests) -> requests
                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                .requestMatchers("/api/author/**").hasRole("AUTHOR")
                                .requestMatchers("/api/user/**").hasAnyRole("USER", "AUTHOR")
                                .requestMatchers("/api/public/**").permitAll()
                                .requestMatchers("/api/auth/public/**").permitAll()
                                .anyRequest()
                                .authenticated());
        http.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler));
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.csrf(AbstractHttpConfigurer::disable);
        http.httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository,
                                      UserRepository userRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_USER)));

            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN)));

            Role authorRole = roleRepository.findByRoleName(AppRole.ROLE_AUTHOR)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_AUTHOR)));


            if (!userRepository.existsByUserName("user1")) {
                 User user1 = new User("user1", "user1@example.com",
                        passwordEncoder.encode("password1")
                );
                user1.setAccountNonLocked(false);
                user1.setAccountNonExpired(true);
                user1.setCredentialsNonExpired(true);
                user1.setEnabled(true);
                user1.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
                user1.setAccountExpiryDate(LocalDate.now().plusYears(1));
                user1.setTwoFactorEnabled(false);
                user1.setSignUpMethod("email");
                user1.setRole(userRole);
                userRepository.save(user1);
            }

            if (!userRepository.existsByUserName("admin")) {
                User admin = new User("admin", "admin@example.com",
                        passwordEncoder.encode("adminPass")
                );
                admin.setAccountNonLocked(true);
                admin.setAccountNonExpired(true);
                admin.setCredentialsNonExpired(true);
                admin.setEnabled(true);
                admin.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
                admin.setAccountExpiryDate(LocalDate.now().plusYears(1));
                admin.setTwoFactorEnabled(false);
                admin.setSignUpMethod("email");
                admin.setRole(adminRole);
                userRepository.save(admin);
            }
            if (!userRepository.existsByUserName("author")) {
                User author = new User("author", "autor@example.com",
                        passwordEncoder.encode("authorPass")
                );
                author.setAccountNonLocked(true);
                author.setAccountNonExpired(true);
                author.setCredentialsNonExpired(true);
                author.setEnabled(true);
                author.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
                author.setAccountExpiryDate(LocalDate.now().plusYears(1));
                author.setTwoFactorEnabled(false);
                author.setSignUpMethod("email");
                author.setRole(authorRole);
                userRepository.save(author);
            }
        };
    }
}