package com.adcorreajr.clientes.rest;

import com.adcorreajr.clientes.model.entity.Cliente;
import com.adcorreajr.clientes.model.entity.ServicoPrestado;
import com.adcorreajr.clientes.model.repository.ClienteRepository;
import com.adcorreajr.clientes.model.repository.ServicoPrestadoRepository;
import com.adcorreajr.clientes.rest.dto.ServicoPrestadoDTO;
import com.adcorreajr.clientes.util.BigDecimalConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/servicos-prestados")
//@CrossOrigin("http://localhost:4200")
public class ServicoPrestadoController {

    @Autowired
    private ServicoPrestadoRepository servicoPrestadoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private BigDecimalConverter bigDecimalConverter;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServicoPrestado salvar(@RequestBody ServicoPrestadoDTO servicoPrestadoDTO){

        LocalDate data = LocalDate.parse(servicoPrestadoDTO.getData(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Cliente cliente = clienteRepository
                                        .findById(servicoPrestadoDTO.getIdCliente())
                                        .orElseThrow( () ->
                                                new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cliente inexistente.")
                                        );

        ServicoPrestado servicoPrestado = new ServicoPrestado();
        servicoPrestado.setCliente(cliente);
        servicoPrestado.setData(data);
        servicoPrestado.setValor(bigDecimalConverter.converter(servicoPrestadoDTO.getPreco()));
        servicoPrestado.setDescricao(servicoPrestadoDTO.getDescricao());

        return servicoPrestadoRepository.save(servicoPrestado);
    }

    @GetMapping("{id}")
    public ServicoPrestado acharPorId(@PathVariable Integer id){
        return servicoPrestadoRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Servico não encontrado."));
    }

     @GetMapping
    public List<ServicoPrestado> buscarNomeAndData(
        @RequestParam(value = "nome", required = false, defaultValue = "") String nome,
        @RequestParam(value = "mes", required = false) Integer mes
    ){
        return servicoPrestadoRepository.findByNomeClienteAndMes("%" + nome + "%", mes);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizar(@PathVariable Integer id, @RequestBody ServicoPrestado servicoPrestadoAlterado)
    {
        servicoPrestadoRepository.findById(id)
                .map(servicoPrestado -> {
                    servicoPrestadoAlterado.setId(servicoPrestado.getId());
                    servicoPrestadoRepository.save(servicoPrestadoAlterado);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Servico não encontrado."));
    }


    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id){
        servicoPrestadoRepository.findById(id)
                .map(servicoPrestado -> {
                    servicoPrestadoRepository.delete(servicoPrestado);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Servico não encontrado."));
    }
}
