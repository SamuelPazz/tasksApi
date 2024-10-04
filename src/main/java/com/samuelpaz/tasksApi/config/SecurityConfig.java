package com.samuelpaz.tasksApi.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// Bean = O método ira retorna um objeto que será gerenciado pelo Spring como um bean. No caso, o Spring Security usa esses beans para construir a cadeia de segurança e configurar CORS e/ou criptografia de senhas
// @Configuration = Indica que esta classe define beans para o Spring
// @EnableWebSecurity = Habilita a segurança web do Spring Security
// @EnableGlobalMethodSecurity(prePostEnabled = true) = Habilita a segurança baseada em anotações, como @PreAuthorize

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    // Rota pública sem autenticação
    private static final String[] PUBLIC_MATCHERS = {
            "/"
    };
    // Rota pública sem autenticação
    private static final String[] PUBLIC_MATCHERS_POST = {
            "/user",
            "/login"
    };

    // Configura a segurança da aplicação, como permissões e controle de sessão
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // Desativa CSRF e CORS
        http.cors().and().csrf().disable();

        // Define quais rotas precisam de autenticação
        http.authorizeRequests()
                .requestMatchers (HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
                .requestMatchers (PUBLIC_MATCHERS).permitAll()
                .anyRequest().authenticated();

        // Define que sessões não são mantidas pelo servidor (stateless)
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Constrói e retorna a configuração de segurança
        return http.build();
    }

    // Configura o compartilhamento de recursos entre diferentes origens (CORS)
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        // Aplica valores padrão e permite os métodos HTTP listados
        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));

        // Aplica as configurações de CORS para todas as rotas
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // Define uma criptografia de senhas usando o algoritmo BCrypt
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}