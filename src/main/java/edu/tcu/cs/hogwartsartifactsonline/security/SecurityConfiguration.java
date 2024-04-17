package edu.tcu.cs.hogwartsartifactsonline.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {
    @Value("${api.endpoint.base-url}")
    private String baseUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/artifacts/**").permitAll()
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/users/**").hasAuthority("ROLE_admin") // Protect the user endpoint
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/users").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.PUT, this.baseUrl + "/users/**").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.DELETE, this.baseUrl + "/users/**").hasAuthority("ROLE_admin")
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                        //Disallow everything else
                        .anyRequest().authenticated()// Always a good idea to put this at last.
                )
                .headers((headers) -> headers.disable())
                .csrf(csrf -> csrf.disable())
                .httpBasic(withDefaults())
                .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }
}
