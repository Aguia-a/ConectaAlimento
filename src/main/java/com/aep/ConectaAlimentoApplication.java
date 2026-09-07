package com.aep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.aep.ui.AppUi;

@SpringBootApplication
public class ConectaAlimentoApplication {

	public static void main(String[] args) {
		var contexto = SpringApplication.run(ConectaAlimentoApplication.class, args);
		contexto.getBean(AppUi.class).iniciar();
	}

}
