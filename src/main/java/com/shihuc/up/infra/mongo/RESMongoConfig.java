package com.shihuc.up.infra.mongo;

import com.mongodb.MongoClientURI;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @Author: chengsh05
 * @Date: 2019/10/28 9:55
 */
@Configuration
@EnableMongoRepositories(mongoTemplateRef="resMongoTemplate")
public class RESMongoConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix="spring.data.mongodb.res")
    public MongoProperties resMongoProperties() throws Exception{
        MongoProperties pro = new MongoProperties();
        return pro;
    }

    @Primary
    @Bean(name="resMongoTemplate")
    public MongoTemplate resMongoTemplate() throws Exception{
        MongoProperties pro = resMongoProperties();
        MongoDbFactory fac = resMongoFactory(pro);
        return new MongoTemplate(fac);
    }

    @Bean
    @Primary
    public MongoDbFactory resMongoFactory(MongoProperties mongoProperties) throws Exception{
        return new SimpleMongoDbFactory(new MongoClientURI(mongoProperties.getUri()));
    }
}