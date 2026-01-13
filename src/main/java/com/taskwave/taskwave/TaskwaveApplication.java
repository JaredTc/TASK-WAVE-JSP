package com.taskwave.taskwave;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskwaveApplication {

	public static void main(String[] args) {

//        DECLARAMOS VARIABLE DE ENTORNO
        Dotenv dotenv = Dotenv.load();
//        ASIGNAMOS LA VARIABLE
        dotenv.entries().forEach(e ->
                System.setProperty(e.getKey(), e.getValue()));


		SpringApplication.run(TaskwaveApplication.class, args);
	}



}
