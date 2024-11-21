package com.gusta.backend.controller;

import com.gusta.backend.dto.PersonDTO;
import com.gusta.backend.response.Response;
import com.gusta.backend.service.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/person")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PersonController {
    private final PersonService service;

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid PersonDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation failed");
        }

        Response<PersonDTO> response = service.create(dto);

        return response.isSuccess() ?
                ResponseEntity.status(HttpStatus.CREATED).body(response.getData()) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getMessage());
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable long id) {
        Response<PersonDTO> response = service.findById(id);

        if (!response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body(response.getData());
    }

    @GetMapping
    public ResponseEntity findAll(@RequestParam(name = "page", defaultValue = "0") int page,
                                  @RequestParam(name = "size", defaultValue = "10") int size) {
        Response<List<PersonDTO>> response = service.findAll(page, size);

        return ResponseEntity.status(HttpStatus.OK).body(response.getData());
    }

    @PutMapping("/{id}")
    public ResponseEntity updateById(@PathVariable long id, @RequestBody @Valid PersonDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation failed");
        }

        Response<PersonDTO> response = service.updateById(id, dto);

        if (!response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getMessage());
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteById(@PathVariable long id) {
        service.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
