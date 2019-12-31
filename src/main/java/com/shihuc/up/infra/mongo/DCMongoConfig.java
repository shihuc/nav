package com.shihuc.up.infra.mongo;

import com.mongodb.MongoClientURI;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @Author: chengsh05
 * @Date: 2019/10/28 9:55
 */
@Configuration
@EnableMongoRepositories(mongoTemplateRef="dcMongoTemplate")
public class DCMongoConfig {

    @Bean
    // @Primary
    @ConfigurationProperties(prefix="spring.data.mongodb.dc")
    public MongoProperties dcMongoProperties() throws Exception{
        MongoProperties pro = new MongoProperties();
        return pro;
    }

    // @Primary
    @Bean(name="dcMongoTemplate")
    public MongoTemplate dcMongoTemplate() throws Exception{
        MongoProperties pro = dcMongoProperties();
        MongoDbFactory fac = dcMongoFactory(pro);
        return new MongoTemplate(fac);
    }

    @Bean
    // @Primary
    public MongoDbFactory dcMongoFactory(MongoProperties mongoProperties) throws Exception{
        return new SimpleMongoDbFactory(new MongoClientURI(mongoProperties.getUri()));
    }
}
