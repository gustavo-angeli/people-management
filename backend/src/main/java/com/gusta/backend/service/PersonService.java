package com.gusta.backend.service;

import com.gusta.backend.dto.PersonDTO;
import com.gusta.backend.response.Response;
import com.gusta.backend.model.Person;
import com.gusta.backend.repository.PersonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        Optional<Person> optionalPerson = repository.findById(id);
        if (optionalPerson.isEmpty()) {
            return Response.failure("Nonexistent person");
        }
        Person entity = optionalPerson.get();
        return Response.success(new PersonDTO(entity.getId(), entity.getName(), entity.getEmail(), entity.getPassword()));
    }
    public Response<List<PersonDTO>> findAll(int page, int size) {
        return Response.success(
                repository.findAllSortedById(Pageable.ofSize(size).withPage(page))
                        .stream().map(
                                e -> new PersonDTO(e.getId(), e.getName(), e.getEmail(), e.getPassword())
                        )
                        .toList()
        );
    }
    public Response<PersonDTO> updateById(long id, PersonDTO dto) {
        if (!repository.existsById(id)) {
            return Response.failure("Nonexistent person");
        }
        if (repository.existsByEmail(dto.getEmail()) && !repository.isUserEmailByUserId(id, dto.getEmail())) {
            return Response.failure("This email already been used");
        }

        Person entity = repository.findById(id).get();

        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());

        return Response.success(null);
    }
    public Response<PersonDTO> deleteById(long id) {
        if (!repository.existsById(id)) {
            return Response.failure("Nonexistent person");
        }
        repository.deleteById(id);
        return Response.success(null);
    }
}
