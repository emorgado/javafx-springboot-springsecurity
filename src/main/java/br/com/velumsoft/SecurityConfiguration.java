package br.com.velumsoft;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true)
public class SecurityConfiguration extends GlobalMethodSecurityConfiguration {

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        
        //super.configure(auth);
        
        auth.inMemoryAuthentication()
            .withUser("user").password("p").roles("USER")
            .and()
            .withUser("admin").password("p").roles("ADMIN");
        
    }
  
 
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
