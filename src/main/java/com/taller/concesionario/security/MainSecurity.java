package com.taller.concesionario.security;

import com.taller.concesionario.security.jwt.JwtEntryPoint;
import com.taller.concesionario.security.jwt.JwtTokenFilter;
import com.taller.concesionario.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

@Configuration
@EnableWebSecurity
//con perPostEnabled se usa para indicar a q metodos puede acceder solo el admin
// Los metodos que no lleven anotación pueden acceder el admin como un generic user
// @preauthorized solo puede acceder el admin
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MainSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    //Devuelve el mensaje de no autorizado
    @Autowired
    JwtEntryPoint jwtEntryPoint;

    @Bean
    public JwtTokenFilter jwtTokenFilter(){
        return new JwtTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    //line 69 todo lo que venga por /auth puede hacerse sin token, line 70 el resto requiere token
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Desactivamos cookies ya que enviamos un token
            // cada vez que hacemos una petición
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

		http.headers().addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Origin", "*"));
		http.headers().addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, PATCH, DELETE"));
		http.headers().addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Headers", "*"));
    }
}
