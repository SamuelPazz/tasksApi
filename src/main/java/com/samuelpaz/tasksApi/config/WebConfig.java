package com.samuelpaz.tasksApi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration = Indica que esta classe contém definições de configuração do Spring
// @EnableWebMvc = Habilita a configuração do Spring MVC na aplicação
// @Override = Sobrescreve o método addCorsMapping da interface WebMvcConfigurer
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    // Implementação da interface WebMvcConfigurer para configurar o comportamento do MVC

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
        // Permite requisições de todas as origens (domínios) para todas as rotas da api (/**)
    }
}
