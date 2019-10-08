package cn.linj2n.spring.security.config;

import cn.linj2n.spring.security.config.support.GithubAuthoritiesExtractor;
import cn.linj2n.spring.security.config.support.GithubPrincipalExtractor;
import cn.linj2n.spring.security.service.security.CustomUserInfoTokenService;
import cn.linj2n.spring.security.service.security.SocialUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.filter.CompositeFilter;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableOAuth2Client  // 启用 OAuth 2.0 客户端
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String OAUTH2_DEFAULT_SUCCESS_URL = "/user/me";

    @Qualifier("oauth2ClientContext")
    @Autowired
    private OAuth2ClientContext oauth2ClientContext;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class)
            .antMatcher("/**")
            .authorizeRequests()
            .antMatchers("/login**", "/403","/css/**", "/js/**", "/fonts/**").permitAll()
            .anyRequest()
            .authenticated()
            .and()
                .logout()
                .logoutSuccessUrl("/login").permitAll()
            .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/user/me")
            .and()
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        ;
    }


    private Filter ssoFilter() {
        CompositeFilter filter = new CompositeFilter();
        List<Filter> filters = new ArrayList<>();
        filters.add(ssoFilter(github(), "/login/github", githubPrincipalExtractor(), githubAuthoritiesExtractor()));
        // add other ssoFilters eg. facebook, google...
        filter.setFilters(filters);
        return filter;
    }

    private Filter ssoFilter(ClientResources client, String path, PrincipalExtractor principalExtractor, AuthoritiesExtractor authoritiesExtractor) {
        OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(path);
        OAuth2RestTemplate template = new OAuth2RestTemplate(client.getClient(), oauth2ClientContext);
        filter.setRestTemplate(template);
        CustomUserInfoTokenService tokenServices = new SocialUserDetailsService(
                client.getResource().getUserInfoUri(), client.getClient().getClientId());
        tokenServices.setRestTemplate(template);
        tokenServices.setPrincipalExtractor(principalExtractor);
        tokenServices.setAuthoritiesExtractor(authoritiesExtractor);
        filter.setTokenServices(tokenServices);
        filter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                this.setDefaultTargetUrl(OAUTH2_DEFAULT_SUCCESS_URL);
                super.onAuthenticationSuccess(request, response, authentication);
            }
        });

        return filter;
    }

    @Bean
    @ConfigurationProperties("github")
    public ClientResources github() {
        return new ClientResources();
    }

    class ClientResources {

        @NestedConfigurationProperty
        private AuthorizationCodeResourceDetails client = new AuthorizationCodeResourceDetails();

        @NestedConfigurationProperty
        private ResourceServerProperties resource = new ResourceServerProperties();

        public AuthorizationCodeResourceDetails getClient() {
            return client;
        }

        public ResourceServerProperties getResource() {
            return resource;
        }
    }

    @Bean
    public PrincipalExtractor githubPrincipalExtractor() {
        return new GithubPrincipalExtractor();
    }

    @Bean
    public AuthoritiesExtractor githubAuthoritiesExtractor() {
        return new GithubAuthoritiesExtractor();
    }

}
