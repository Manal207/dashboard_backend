package tech.webapp.opticsmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import tech.webapp.opticsmanager.security.JwtAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Get allowed origins from environment variable, fallback to localhost
        String allowedOrigins = System.getenv("ALLOWED_ORIGINS");
        if (allowedOrigins != null && !allowedOrigins.isEmpty()) {
            configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        } else {
            // Default for development
            configuration.setAllowedOrigins(Arrays.asList(
                    "http://localhost:3000",
                    "https://dashboard-seven-snowy-80.vercel.app"
            ));
        }

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        // Public endpoints - must be first
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/debug/**").permitAll()
                        .requestMatchers("/api/test/public").permitAll()

                        // ✅ ADD THIS LINE - Allow public access to images
                        .requestMatchers("/monture/images/**").permitAll()
                        .requestMatchers("/uploads/**").permitAll() // Also allow uploads if needed

                        // Admin-only endpoints - must come before general authenticated
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/add").hasRole("ADMIN")
                        .requestMatchers("/user/delete/**").hasRole("ADMIN")
                        .requestMatchers("/user/all").hasRole("ADMIN")

                        // Authenticated user endpoints
                        .requestMatchers("/api/test/**").authenticated()
                        .requestMatchers("/user/find/**").authenticated()
                        .requestMatchers("/user/update").authenticated()

                        // All other endpoints require authentication
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
















////package tech.webapp.opticsmanager.config;
////
////import org.springframework.context.annotation.Bean;
////import org.springframework.context.annotation.Configuration;
////import org.springframework.security.authentication.AuthenticationManager;
////import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
////import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
////import org.springframework.security.config.annotation.web.builders.HttpSecurity;
////import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
////import org.springframework.security.config.http.SessionCreationPolicy;
////import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
////import org.springframework.security.crypto.password.PasswordEncoder;
////import org.springframework.security.web.SecurityFilterChain;
////import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
////import org.springframework.web.cors.CorsConfiguration;
////import org.springframework.web.cors.CorsConfigurationSource;
////import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
////import tech.webapp.opticsmanager.security.JwtAuthenticationFilter;
//package tech.webapp.opticsmanager.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import tech.webapp.opticsmanager.security.JwtAuthenticationFilter;
//
//import java.util.Arrays;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity(prePostEnabled = true)
//public class SecurityConfig {
//
//    @Bean
//    public JwtAuthenticationFilter jwtAuthenticationFilter() {
//        return new JwtAuthenticationFilter();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//        return authConfig.getAuthenticationManager();
//    }
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//
//        // Get allowed origins from environment variable, fallback to localhost
//        String allowedOrigins = System.getenv("ALLOWED_ORIGINS");
//        if (allowedOrigins != null && !allowedOrigins.isEmpty()) {
//            configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
//        } else {
//            // Default for development
//            configuration.setAllowedOrigins(Arrays.asList(
//                    "http://localhost:3000",
//                    "https://dashboard-seven-snowy-80.vercel.app"
//            ));
//        }
//
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        configuration.setAllowedHeaders(Arrays.asList("*"));
//        configuration.setExposedHeaders(Arrays.asList("Authorization"));
//        configuration.setAllowCredentials(true);
//        configuration.setMaxAge(3600L);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//                .csrf(csrf -> csrf.disable())
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(authz -> authz
//                        // Public endpoints
//                        .requestMatchers("/api/auth/**").permitAll()
//                        .requestMatchers("/debug/**").permitAll()
//                        .requestMatchers("/api/test/public").permitAll()
//
//                        // Admin-only endpoints
//                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/user/add").hasRole("ADMIN")
//                        .requestMatchers("/user/delete/**").hasRole("ADMIN")
//                        .requestMatchers("/user/all").hasRole("ADMIN")
//
//                        // Authenticated user endpoints
//                        .requestMatchers("/api/test/user").authenticated()
//                        .requestMatchers("/user/find/**").authenticated()
//                        .requestMatchers("/user/update").authenticated()
//
//                        // All other endpoints require authentication
//                        .anyRequest().authenticated()
//                )
//                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//}
////
////import java.util.Arrays;
////
////@Configuration
////@EnableWebSecurity
////@EnableMethodSecurity(prePostEnabled = true)
////public class SecurityConfig {
////
////    @Bean
////    public JwtAuthenticationFilter jwtAuthenticationFilter() {
////        return new JwtAuthenticationFilter();
////    }
////
////    @Bean
////    public PasswordEncoder passwordEncoder() {
////        return new BCryptPasswordEncoder();
////    }
////
////    @Bean
////    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
////        return authConfig.getAuthenticationManager();
////    }
////
////    @Bean
////    public CorsConfigurationSource corsConfigurationSource() {
////        CorsConfiguration configuration = new CorsConfiguration();
////
////        // Get allowed origins from environment variable, fallback to localhost
////        String allowedOrigins = System.getenv("ALLOWED_ORIGINS");
////        if (allowedOrigins != null && !allowedOrigins.isEmpty()) {
////            configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
////        } else {
////            // Default for development
////            configuration.setAllowedOrigins(Arrays.asList(
////                    "http://localhost:3000",
////                    "https://dashboard-seven-snowy-80.vercel.app"
////            ));
////        }
////
////        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
////        configuration.setAllowedHeaders(Arrays.asList("*"));
////        configuration.setExposedHeaders(Arrays.asList("Authorization"));
////        configuration.setAllowCredentials(true);
////        configuration.setMaxAge(3600L);
////
////        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
////        source.registerCorsConfiguration("/**", configuration);
////        return source;
////
//////        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//////        source.registerCorsConfiguration("/**", source);
//////        return source;
////    }
////
////    @Bean
////    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////        http
////                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
////                .csrf(csrf -> csrf.disable())
////                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
////                .authorizeHttpRequests(authz -> authz
////                        // Public endpoints
////                        .requestMatchers("/api/auth/**").permitAll()
////                        .requestMatchers("/debug/**").permitAll()
////                        .requestMatchers("/api/test/public").permitAll()
////
////                        // Admin-only endpoints
////                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
////                        .requestMatchers("/user/add").hasRole("ADMIN")
////                        .requestMatchers("/user/delete/**").hasRole("ADMIN")
////                        .requestMatchers("/user/all").hasRole("ADMIN")
////
////                        // Authenticated user endpoints
////                        .requestMatchers("/api/test/user").authenticated()
////                        .requestMatchers("/user/find/**").authenticated()
////                        .requestMatchers("/user/update").authenticated()
////
////                        // All other endpoints require authentication
////                        .anyRequest().authenticated()
////                )
////                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
////
////        return http.build();
////    }
////}
////
////
////
////
////
////
////
////
//////package tech.webapp.opticsmanager.config;
//////
//////import org.springframework.context.annotation.Bean;
//////import org.springframework.context.annotation.Configuration;
//////import org.springframework.security.authentication.AuthenticationManager;
//////import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//////import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//////import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//////import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//////import org.springframework.security.config.http.SessionCreationPolicy;
//////import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//////import org.springframework.security.crypto.password.PasswordEncoder;
//////import org.springframework.security.web.SecurityFilterChain;
//////import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//////import org.springframework.web.cors.CorsConfiguration;
//////import org.springframework.web.cors.CorsConfigurationSource;
//////import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
////
////
////
////
////
//////import tech.webapp.opticsmanager.security.JwtAuthenticationFilter;
//////
//////import java.util.Arrays;
//////
//////@Configuration
//////@EnableWebSecurity
//////@EnableMethodSecurity(prePostEnabled = true)
//////public class SecurityConfig {
//////
//////    @Bean
//////    public JwtAuthenticationFilter jwtAuthenticationFilter() {
//////        return new JwtAuthenticationFilter();
//////    }
//////
//////    @Bean
//////    public PasswordEncoder passwordEncoder() {
//////        return new BCryptPasswordEncoder();
//////    }
//////
//////    @Bean
//////    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//////        return authConfig.getAuthenticationManager();
//////    }
//////
////////    @Bean
////////    public CorsConfigurationSource corsConfigurationSource() {
////////        CorsConfiguration configuration = new CorsConfiguration();
////////        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
////////        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
////////        configuration.setAllowedHeaders(Arrays.asList("*"));
////////        configuration.setExposedHeaders(Arrays.asList("Authorization"));
////////        configuration.setAllowCredentials(true);
////////        configuration.setMaxAge(3600L);
////////
////////        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
////////        source.registerCorsConfiguration("/**", configuration);
////////        return source;
////////    }
//////
//////    @Bean
//////    public CorsConfigurationSource corsConfigurationSource() {
//////        CorsConfiguration configuration = new CorsConfiguration();
//////
//////        // Get allowed origins from environment variable, fallback to localhost
//////        String allowedOrigins = System.getenv("ALLOWED_ORIGINS");
//////        if (allowedOrigins != null && !allowedOrigins.isEmpty()) {
//////            configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
//////        } else {
//////            // Default for development
//////            configuration.setAllowedOrigins(Arrays.asList(
//////                    "http://localhost:3000",
//////                    "https://dashboard-seven-snowy-80.vercel.app"
//////            ));
//////        }
//////
//////        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//////        configuration.setAllowedHeaders(Arrays.asList("*"));
//////        configuration.setExposedHeaders(Arrays.asList("Authorization"));
//////        configuration.setAllowCredentials(true);
//////        configuration.setMaxAge(3600L);
//////
//////        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//////        source.registerCorsConfiguration("/**", configuration);
//////        return source;
//////    }
//////
//////    @Bean
//////    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//////        http
//////                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//////                .csrf(csrf -> csrf.disable())
//////                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//////                .authorizeHttpRequests(authz -> authz
//////                        .requestMatchers("/api/auth/**").permitAll()
//////                        .requestMatchers("/**").authenticated()
//////                )
//////                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//////
//////        return http.build();
//////    }
//////}
//////
//////
//////
//////
////////package tech.webapp.opticsmanager.config;
////////
////////import org.springframework.context.annotation.Bean;
////////import org.springframework.context.annotation.Configuration;
////////import org.springframework.security.authentication.AuthenticationManager;
////////import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
////////import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
////////import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
////////import org.springframework.security.config.annotation.web.builders.HttpSecurity;
////////import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
////////import org.springframework.security.config.http.SessionCreationPolicy;
////////import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
////////import org.springframework.security.crypto.password.PasswordEncoder;
////////import org.springframework.security.web.SecurityFilterChain;
////////import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
////////import tech.webapp.opticsmanager.security.JwtAuthenticationFilter;
////////
////////@Configuration
////////@EnableWebSecurity
////////@EnableMethodSecurity(prePostEnabled = true)
////////public class SecurityConfig {
////////
////////    @Bean
////////    public JwtAuthenticationFilter jwtAuthenticationFilter() {
////////        return new JwtAuthenticationFilter();
////////    }
////////
////////    @Bean
////////    public PasswordEncoder passwordEncoder() {
////////        return new BCryptPasswordEncoder();
////////    }
////////
////////    @Bean
////////    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
////////        return authConfig.getAuthenticationManager();
////////    }
////////
//////////    @Bean
//////////    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//////////        http.cors().and().csrf().disable()
//////////                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//////////                .authorizeHttpRequests(authz -> authz
//////////                        .requestMatchers("/api/auth/**").permitAll()
//////////                        .requestMatchers("/api/test/**").permitAll()
//////////                        .anyRequest().authenticated()
//////////                );
//////////
//////////        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//////////
//////////        return http.build();
//////////    }
////////
////////    @Bean
////////    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////////        http
////////                .authorizeHttpRequests(authz -> authz
////////                        .requestMatchers("/api/auth/**").permitAll()
//////////                        .requestMatchers("/api/test/**").permitAll()
////////                        .requestMatchers(
////////                                "/monture/**",
////////                                "/verre/**",
////////                                "/lentille/**",
////////                                "/accessoire/**",
////////                                "/vente/**",
////////                                "/magasin/**",
////////                                "/particulier/**",
////////                                "/fournisseur/**",
////////                                "/ligne-vente/**",
////////                                "/api/**"
////////                        ).authenticated()
////////                        .anyRequest().authenticated()
////////                )
////////                .csrf(csrf -> csrf.disable())
////////                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
////////                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
////////
////////        return http.build();
////////    }
////////
////////}