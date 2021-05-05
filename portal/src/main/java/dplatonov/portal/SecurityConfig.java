package dplatonov.portal;

import dplatonov.portal.enums.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final DataSource dataSource;

  @Value("${spring.queries.users-query}")
  private String usersQuery;

  @Value("${spring.queries.roles-query}")
  private String rolesQuery;

  @Autowired
  public SecurityConfig(BCryptPasswordEncoder bCryptPasswordEncoder, DataSource dataSource) {
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.dataSource = dataSource;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.jdbcAuthentication()
        .usersByUsernameQuery(usersQuery)
        .authoritiesByUsernameQuery(rolesQuery)
        .dataSource(dataSource)
        .passwordEncoder(bCryptPasswordEncoder);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.authorizeRequests()
        .antMatchers(HttpMethod.POST, "/api/login")
        .permitAll()
        .antMatchers(HttpMethod.PUT, "/api/registration")
        .permitAll()
        .antMatchers("index.html")
        .permitAll()
        .antMatchers("/actuator/info")
        .permitAll()
        .antMatchers("/**")
        .authenticated()
        .antMatchers("/user")
        .hasRole(RoleEnum.getRoleEnum(RoleEnum.ADMIN))
        .and()
        .httpBasic()
        .and()
        .csrf()
        .disable()
        .logout()
        .logoutSuccessUrl("/")
        .and()
        .exceptionHandling()
        .accessDeniedPage("/access-denied");
  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
  }
}
