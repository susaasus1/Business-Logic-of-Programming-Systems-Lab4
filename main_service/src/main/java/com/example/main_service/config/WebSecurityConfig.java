package com.example.main_service.config;

import com.example.main_service.model.Recipe;
import com.example.main_service.security.AuthEntryPointJwt;
import com.example.main_service.security.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.List;

@EnableWebSecurity
@EnableTransactionManagement
public class WebSecurityConfig {
    private final AuthEntryPointJwt unauthorizedHandler;

    private final AuthTokenFilter authenticationJwtTokenFilter;


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("localhost"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Autowired
    public WebSecurityConfig(AuthEntryPointJwt unauthorizedHandler, AuthTokenFilter authenticationJwtTokenFilter) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.authenticationJwtTokenFilter = authenticationJwtTokenFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public Recipe singletonRecipe() {
        return new Recipe();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().configurationSource(corsConfigurationSource()).and().csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests()
                .antMatchers("/cuisine/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.GET, "/cuisine/**").hasAuthority("ROLE_USER")

                .antMatchers(HttpMethod.POST, "/culinary-news/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers(HttpMethod.GET, "/culinary-news/**").permitAll()

                .antMatchers("/dish/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.GET, "/dish/**").hasAuthority("ROLE_USER")

                .antMatchers("/ingredient/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.GET, "/ingredient/**").hasAuthority("ROLE_USER")

                .antMatchers("/taste/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.GET, "/taste/**").hasAuthority("ROLE_USER")

                .antMatchers(HttpMethod.GET, "/recipe").permitAll()
                .antMatchers(HttpMethod.POST, "/recipe").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers(HttpMethod.DELETE, "/recipe").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers(HttpMethod.PUT, "/recipe").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers("/recipe/review").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.PUT, "/recipe/accept/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.DELETE, "/recipe/decline/**").hasAuthority("ROLE_ADMIN")

                .antMatchers(HttpMethod.POST, "/user/admin-create").hasAuthority("ROLE_ADMIN")
                .anyRequest().permitAll();

        http.addFilterBefore(authenticationJwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
