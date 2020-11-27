package io.terminus.addressApiTest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class AddressApiTestApplication {


	public static void main(String[] args) {
		SpringApplication.run(AddressApiTestApplication.class, args);
	}

}
