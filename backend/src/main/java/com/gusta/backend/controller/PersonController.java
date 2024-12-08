package com.gusta.backend.controller;

import com.gusta.backend.Utils;
import com.gusta.backend.dto.PersonDTO;
import com.gusta.backend.response.Response;
import com.gusta.backend.service.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/person")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PersonController {
    private final PersonService service;

    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody @Valid PersonDTO dto,
            BindingResult bindingResult
    ) throws URISyntaxException {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }

        Response<PersonDTO> response = service.create(dto);

        return response.isSuccess() ?
                ResponseEntity.created(new URI("api/person/" + response.getData().getId())).body(response.getData()) :
                ResponseEntity.badRequest().body(response.getMessage());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        if (!Utils.isNumeric(id)) {
            return ResponseEntity.badRequest().body("Given Id is not a number");
        }

        Response<PersonDTO> response = service.findById(Long.parseLong(id));

        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response.getMessage());
        }

        return ResponseEntity.ok().body(response.getData());
    }

    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(name = "page", defaultValue = "0") String page,
            @RequestParam(name = "size", defaultValue = "10") String size
    ) {
        if (!Utils.isNumeric(page) || !Utils.isNumeric(size)) {
            return ResponseEntity.badRequest().body("Given page number / page size is not a number");
        }

        int pageNumber = Integer.parseInt(page);
        int pageSize = Integer.parseInt(size);

        if (pageSize < 1) {
            return ResponseEntity.badRequest().body("Page size must not be less than one");
        }
        if (pageNumber < 0) {
            return ResponseEntity.badRequest().body("Page index must not be less than zero");
        }

        Response<List<PersonDTO>> response = service.findAll(pageNumber, pageSize);

        return ResponseEntity.ok().body(response.getData());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(
            @PathVariable String id,
            @RequestBody @Valid PersonDTO dto,
            BindingResult bindingResult
    ) {
        if (!Utils.isNumeric(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Given Id is not a number");
        }

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        Response<PersonDTO> response = service.updateById(Long.parseLong(id), dto);

        if (!response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getMessage());
        }

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        if (!Utils.isNumeric(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Given Id is not a number");
        }

        Response<PersonDTO> response = service.deleteById(Long.parseLong(id));

        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response.getMessage());
        }

        return ResponseEntity.noContent().build();
    }
}
