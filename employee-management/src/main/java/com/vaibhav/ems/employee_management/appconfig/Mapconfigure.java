package com.vaibhav.ems.employee_management.appconfig;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Mapconfigure {
       @Bean
        public ModelMapper modelMapper() {

            return new ModelMapper();
        }

}
