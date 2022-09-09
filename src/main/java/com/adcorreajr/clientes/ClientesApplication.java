package com.adcorreajr.clientes;

import com.adcorreajr.clientes.model.entity.Cliente;
import com.adcorreajr.clientes.model.entity.ServicoPrestado;
import com.adcorreajr.clientes.model.repository.ClienteRepository;
import com.adcorreajr.clientes.model.repository.ServicoPrestadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootApplication
public class ClientesApplication {

	@Bean
	public CommandLineRunner run (@Autowired ClienteRepository clienteRepository
			, @Autowired ServicoPrestadoRepository servicoPrestadoRepository){
		return args -> {
			Cliente cliente= Cliente.builder().cpf("36126949870").nome("Antonio Junior").build();
			cliente = clienteRepository.save(cliente);

			ServicoPrestado servicoPrestado = ServicoPrestado.builder()
								.descricao("Formatação PC")
								.cliente(cliente)
								.data(LocalDate.now())
								.valor(new BigDecimal("1000.00"))
								.build();

			servicoPrestadoRepository.save(servicoPrestado);

		};


	}
	public static void main(String[] args) {
		SpringApplication.run(ClientesApplication.class, args);
	}

}
