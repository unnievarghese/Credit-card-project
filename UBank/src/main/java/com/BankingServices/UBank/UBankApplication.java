package com.BankingServices.UBank;

import com.BankingServices.UBank.AutheticateClient.AuthenticateClient;
import com.BankingServices.UBank.Util.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableAutoConfiguration
@EnableFeignClients
public class UBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(UBankApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticateClient authenticateClient(){return new AuthenticateClient();}

	@Bean
	public Utils utils(){
		return new Utils();
	}

}
