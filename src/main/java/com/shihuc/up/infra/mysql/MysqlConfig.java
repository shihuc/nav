package com.shihuc.up.infra.mysql;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @Author: chengsh05
 * @Date: 2018/12/19 9:54
 */
@Configuration
@ImportResource(locations = {"classpath:config/spring-dao.xml"})
public class MysqlConfig {
}