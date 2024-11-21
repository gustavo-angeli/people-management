package com.gusta.backend.service;

import com.gusta.backend.dto.PersonDTO;
import com.gusta.backend.response.Response;
import com.gusta.backend.model.Person;
import com.gusta.backend.repository.PersonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository repository;

    public Response<PersonDTO> create(PersonDTO dto) {
        if (repository.existsByEmail(dto.getEmail())) {
            return Response.failure("This email already been used");
        }

        Person entity = repository.save(new Person(null, dto.getName(), dto.getEmail(), dto.getPassword()));
        dto.setId(entity.getId());

        return Response.success(dto);
    }
    public Response<PersonDTO> findById(long id) {
        Person entity = repository.findById(id).orElse(null);
        if (entity == null) {
            return Response.failure("Nonexistent person");
        }
        return Response.success(new PersonDTO(entity.getId(), entity.getName(), entity.getEmail(), entity.getPassword()));
    }
    public Response<List<PersonDTO>> findAll(int page, int size) {
        List<PersonDTO> personList = new ArrayList<>();
        repository.findAll(Pageable.ofSize(size).withPage(page)).forEach(entity ->
                personList.add(new PersonDTO(entity.getId(), entity.getName(), entity.getEmail(), entity.getPassword()))
        );

        return Response.success(personList);
    }
    public Response<PersonDTO> updateById(long id, PersonDTO dto) {
        if (repository.existsByEmail(dto.getEmail()) && !repository.isUserEmailByUserId(id, dto.getEmail())) {
            return Response.failure("This email already been used");
        }

        Person entity = repository.findById(id).orElse(null);

        if (entity == null) {
            return Response.failure("Nonexistent person");
        }

        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());

        return Response.success(null);
    }
    public void deleteById(long id) {
        repository.deleteById(id);
    }
}
