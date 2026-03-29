// package com.project.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;

// @Configuration
// @EnableWebSecurity
// public class SecurityConfig {
    
//     @Bean
//     public BCryptPasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }
    
//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
            
//         .csrf(csrf -> csrf.disable())
//         .formLogin(form -> form.disable())   // 🔥 ADD THIS
//         .httpBasic(basic -> basic.disable()) // 🔥 ADD THIS
//             .authorizeHttpRequests(auth -> auth
//             .requestMatchers("/", "/index.html").permitAll()  // Allow homepage
//             .requestMatchers("/static/**", "/js/**", "/css/**", "/images/**").permitAll()  // ✅ FIXED (no /**/*.js)
//             .requestMatchers("/api/auth/**").permitAll()  // Auth endpoints
//             .requestMatchers("/api/health/**").permitAll()  // Health check
//             .requestMatchers("/api/plagiarism/**").permitAll()  // 🔥 ADD THIS (important for your upload)
//             .anyRequest().authenticated()
//         )
//             .httpBasic(basic -> {});  // Enable HTTP Basic authentication
        
//         return http.build();
//     }
// }

package com.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    //     http
    //         .csrf(csrf -> csrf.disable())

    //         // 🔥 Disable default auth systems
    //         .formLogin(form -> form.disable())
    //         .httpBasic(basic -> basic.disable())

    //         // 🔥 VERY IMPORTANT for JWT
    //         .sessionManagement(sess -> 
    //             sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    //         )

    //         .authorizeHttpRequests(auth -> auth
    //             .requestMatchers("/", "/index.html").permitAll()
    //             .requestMatchers("/static/**", "/js/**", "/css/**", "/images/**").permitAll()
    //             .requestMatchers("/api/auth/**").permitAll()
    //             .requestMatchers("/api/health/**").permitAll()
    //             .requestMatchers("/api/plagiarism/**").permitAll()
    //             .anyRequest().authenticated()
    //         );

    //     return http.build();
    // }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())   // ✅ fully disabled

            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/index.html").permitAll()
                .requestMatchers("/static/**", "/js/**", "/css/**", "/images/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/plagiarism/**").permitAll()
                .anyRequest().authenticated()
            );

        return http.build();
}
}