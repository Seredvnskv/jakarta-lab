package org.com.jakartalabauth.config;

import org.intellij.lang.annotations.Language;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
public class UserConfig {

    @Language("SQL")
    public static final String AUTHORITIES_BY_USERNAME_QUERY = "select username, 'ROLE_' || role from users where username = ?";

    @Language("SQL")
    public static final String USERS_BY_USERNAME_QUERY = "select username, password, true from users where username = ?";

    @Bean
    public UserDetailsService users(DataSource source) {
        JdbcDaoImpl users = new JdbcDaoImpl();
        users.setDataSource(source);
        users.setUsersByUsernameQuery(USERS_BY_USERNAME_QUERY);
        users.setAuthoritiesByUsernameQuery(AUTHORITIES_BY_USERNAME_QUERY);
        return users;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        DelegatingPasswordEncoder encoder = (DelegatingPasswordEncoder) PasswordEncoderFactories.createDelegatingPasswordEncoder();
        encoder.setDefaultPasswordEncoderForMatches(new JakartaPasswordEncoder());
        return encoder;
    }
}
