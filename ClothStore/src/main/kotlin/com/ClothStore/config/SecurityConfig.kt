package com.ClothStore.config

import com.ClothStore.service.impl.UserSecurityService
import com.ClothStore.utility.SecurityUtility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.util.matcher.AntPathRequestMatcher


// provide the method call @Bean that create
// object that is reuseable
@Configuration
// this method(login, logout, who can access) provide the security method that
// can be use
@EnableWebSecurity
// allow certain user has those functionlity
@EnableGlobalMethodSecurity(prePostEnabled=true)
// WebSecurityConfigurerAdapter provide the require method
// to change the modify the security setting
class SecurityConfig : WebSecurityConfigurerAdapter()
{

    // pass UserSecurityService to the userSecurityService object
    // the object userSecurityService contains data show if the user exit
    @Autowired
    private val userSecurityService: UserSecurityService? = null

    // the method provide BCrypt algorithm that hash the password
    // the result of generate password are pass from SecurityUtility
    private fun passwordEncoder(): BCryptPasswordEncoder {
        return SecurityUtility.passwordEncoder()
    }

    // determine which url can enter without permission
    // and store that to the val
    val accessibleUrls = arrayOf(
        "/css/**",
        "/js/**",
        "/image/**",
        "/",
        "/newUser",
        "/forgetPassword",
        "/login",
        "/fonts/**",
        "/shirtshelf",
        "/shirtDetail",
        "/hours",
        "/faq",
        "/searchByCategory",
        "/searchShirt",
        "/shirtOne",
        "/shirtTwo",
        "/shirtThree",
        "/shirtFour",
        "/shirtFive",
        "/shirtSix",
        "/shirtSeven",
        "/shirtEight",
        "/shirtNine",
        "/shirtTen",
        "/shirtEleven",
        "/shirtTwelve",
        "/shirtThirteen",
        "/shirtFourteen",
        "/shirtFifteen",
        "/shirtSixteen",
        "/shirtSeventeen",
        "/shirtEighteen"
    )

    @Override
    override fun configure(http: HttpSecurity) {
        http
            // setting up rule for web
            .authorizeRequests()
            // allow to view the item on web without login
            .antMatchers(*accessibleUrls)
            .permitAll()
            // this require other to have authenciation
            .anyRequest().authenticated()

        http
            // turn off the protection
            .csrf().disable()
            .cors().disable()
            // allow user to enter the standard user and password
            .formLogin()
            // if fail to login
            .failureUrl("/login?error")
            // default login
            .defaultSuccessUrl("/")
            .loginPage("/login")
            // allow login and logout
            .permitAll()
            .and()
            .logout()
            // create listener when user click logout
            .logoutRequestMatcher(AntPathRequestMatcher("/logout"))
            // show the logout static
            .logoutSuccessUrl("/?logout")
            // delete the cookie from site
            .deleteCookies("JSESSIONID", "remember-me")
            // permit all with authenciation and remember that
            .permitAll()
            .and()
            .rememberMe()
    }

    // check if the user is valid and so login
    @Autowired
    @Throws(java.lang.Exception::class)
    fun configureGlobal(authentication: AuthenticationManagerBuilder) {
        authentication.userDetailsService<UserDetailsService>(userSecurityService).passwordEncoder(passwordEncoder())
    }

}