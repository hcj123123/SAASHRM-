    package com.ihrm.company;


    import com.ihrm.common.utils.IdWorker;
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import org.springframework.boot.autoconfigure.domain.EntityScan;
    import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
    import org.springframework.context.annotation.Bean;


    //配置springboot包扫描
    @SpringBootApplication(scanBasePackages = "com.ihrm.company")
    @EntityScan(value = "com.ihrm.domain.company")
    @EnableEurekaClient
    public class CompanyApplication {

        public static void main(String[] args) {
            SpringApplication.run(CompanyApplication.class,args);
        }

        @Bean
        public IdWorker idWorker()
        {
            return  new IdWorker();
        }
    }
