// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.multitenant.async;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.talend.daikon.multitenant.context.DefaultTenancyContext;
import org.talend.daikon.multitenant.context.TenancyContext;
import org.talend.daikon.multitenant.context.TenancyContextHolder;
import org.talend.daikon.multitenant.provider.DefaultTenant;

import jakarta.servlet.ServletRequest;

@SpringBootApplication
@Import(MultiTenantApplication.CustomSecurityConfiguration.class)
public class MultiTenantApplication {

    @AutoConfiguration
    public class CustomSecurityConfiguration {

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public WebSecurityCustomizer webSecurityCustomizer() {
            return (web) -> web.ignoring().requestMatchers("/public/**");
        }

        @Bean("securityFilterChain.basic")
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(a -> a.anyRequest().authenticated())
                    .httpBasic(withDefaults());
            return http.build();
        }

        @Bean
        public UserDetailsManager users(PasswordEncoder passwordEncoder) {
            String password = passwordEncoder.encode("password");
            UserDetails user = User.builder().username("user").password(password).authorities("ROLE_USER").build();
            return new InMemoryUserDetailsManager(user);
        }
    }

    @RestController
    public static class MessagingEndpoint {

        @Autowired
        private MessagingService messagingService;

        @RequestMapping(method = RequestMethod.POST, path = "/public/{tenant}")
        public void sendMessage(@PathVariable("tenant") String tenant, @RequestParam("priority") String priority,
                @RequestBody String message, ServletRequest request) {
            if (priority != null) {
                request.setAttribute("priority", priority);
            }
            TenancyContext context = new DefaultTenancyContext();
            context.setTenant(new DefaultTenant(tenant, null));
            TenancyContextHolder.setContext(context);
            messagingService.sendMessage(message);
        }

        @RequestMapping(method = RequestMethod.POST, path = "/private/{tenant}")
        public void securedSendMessage(@PathVariable("tenant") String tenant, @RequestBody String message) {
            TenancyContext context = new DefaultTenancyContext();
            context.setTenant(new DefaultTenant(tenant, null));
            TenancyContextHolder.setContext(context);
            messagingService.sendMessage(message);
        }
    }

    @Service
    public static class MessagingService {

        @Autowired
        private MessageQueue messageQueue;

        public void sendMessage(String message) {
            this.messageQueue.publish(message);
        }

        public Message receiveMessage() throws InterruptedException {
            return this.messageQueue.receive();
        }
    }

    @Component
    public static class MessageQueue {

        private final LinkedBlockingQueue<Message> messages = new LinkedBlockingQueue<>(1);

        @Autowired
        private MessagePublicationHandler handler;

        @Async("contextAwarePoolExecutor")
        public void publish(String content) {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            String priority = null;
            if (requestAttributes != null) {
                priority = (String) requestAttributes.getAttribute("priority", RequestAttributes.SCOPE_REQUEST);
            }
            String tenantId = String.valueOf(TenancyContextHolder.getContext().getTenant().getIdentity());

            String userId = null;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                userId = authentication.getName();
            }

            Message message = new Message(userId, tenantId, content, priority);
            this.messages.add(message);
            this.handler.onMessagePublished();
        }

        public Message receive() throws InterruptedException {
            return messages.poll(5000, TimeUnit.MILLISECONDS);
        }
    }

    public interface MessagePublicationHandler {

        void onMessagePublished();
    }

    public static class Message {

        private final String userId;

        private final String tenantId;

        private final String content;

        private final String priority;

        private Message(String userId, String tenantId, String content, String priority) {
            this.userId = userId;
            this.tenantId = tenantId;
            this.content = content;
            this.priority = priority;
        }

        public String getTenantId() {
            return tenantId;
        }

        public String getContent() {
            return content;
        }

        public String getPriority() {
            return priority;
        }

        public String getUserId() {
            return userId;
        }
    }
}
