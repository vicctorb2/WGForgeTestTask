package com.vicctorb.wgtesttask36;

import com.vicctorb.wgtesttask36.controller.RequestsCountController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
@EnableJpaAuditing
public class WgTestTask36Application {

	@Bean
	public RequestsCountController getRequesCountController(){
		return RequestsCountController.getInstance();
	}

	public static void main(String[] args) {
		SpringApplication.run(WgTestTask36Application.class, args);

		//this timer collects the requests count and resets every minute
		TimerTask timerTask = RequestsCountController.getInstance();
		Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(timerTask, 0, 60*1000);
	}

}
