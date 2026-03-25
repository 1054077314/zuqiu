package com;
                                                                                                                                          
                                                                                                                                                      
import org.mybatis.spring.annotation.MapperScan;                                                                                                      
import org.springframework.boot.SpringApplication;                                                                                                    
import org.springframework.boot.autoconfigure.SpringBootApplication;                                                                                  
import org.springframework.boot.builder.SpringApplicationBuilder;                                                                                     
import org.springframework.boot.web.servlet.ServletComponentScan;                                                                                     
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;                                                                     
                                                                                                                                                      
@SpringBootApplication                                                                                                                                
@ServletComponentScan(value = "com.listener")                                                                                                         
@MapperScan(basePackages = {"com.dao"})                                                                                                               
public class ZuqiujulebguanliApplication extends SpringBootServletInitializer {                                                                       
                                                                                                                                                      
    public static void main(String[] args) {                                                                                                          
        SpringApplication.run(ZuqiujulebguanliApplication.class, args);                                                                               
    }                                                                                                                                                 
                                                                                                                                                      
    @Override                                                                                                                                         
    protected SpringApplicationBuilder configure(SpringApplicationBuilder applicationBuilder) {                                                       
        return applicationBuilder.sources(ZuqiujulebguanliApplication.class);                                                                         
    }                                                                                                                                                 
}                                                                                                                                                     
                                                                                                                                                      