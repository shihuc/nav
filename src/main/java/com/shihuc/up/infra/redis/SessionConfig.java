package com.shihuc.up.infra.redis;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
//import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration;
//
//import javax.annotation.PostConstruct;
//
///**
// * @Author: chengsh05
// * @Date: 2018/12/19 12:19
// *
// * 配置利用redis实现session共享，基于springsession
// */
////@Configuration
////@EnableRedisHttpSession(redisNamespace="iotscc@session")
////public class SessionConfig {
////
////    @Autowired
////    private RedisHttpSessionConfiguration redisHttpSessionConfiguration;
////
////    @Autowired
////    private CookieSerializer cookieSerializer;
////
////    @Value("${session.redis.host}")
////    private String hostName;
////    @Value("${session.redis.port}")
////    private int port;
////    @Value("${session.redis.database}")
////    private int db;
////    @Value("${session.redis.password}")
////    private String pass;
////    @Value("${server.session.cookie.max-age}")
////    private int maxAge;
////    @Value("${server.session.maxInactiveInterval}")
////    private int maxInactiveInterval;
////
////
////    @Bean
////    public JedisConnectionFactory connectionFactory() {
////        JedisConnectionFactory connection = new JedisConnectionFactory();
////        connection.setPort(port);
////        connection.setHostName(hostName);
////        connection.setDatabase(db);
////        connection.setPassword(pass);
////        return connection;
////    }
////
////    @PostConstruct
////    public void config() {
////        cookieSerializer.setCookieMaxAge(maxAge);
////        redisHttpSessionConfiguration.setCookieSerializer(cookieSerializer);
////        redisHttpSessionConfiguration.setMaxInactiveIntervalInSeconds(maxInactiveInterval);
////    }
////}