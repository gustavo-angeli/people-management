package com.gusta.backend.service;

import com.gusta.backend.dto.PersonDTO;
import com.gusta.backend.model.Person;
import com.gusta.backend.repository.PersonRepository;
import com.gusta.backend.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {
    @Mock
    private PersonRepository repository;
    @InjectMocks
    private PersonService service;

    Person person = new Person(1L, "Test Person", "person@person.com", "12345678");
    PersonDTO personDTO = new PersonDTO(null, "Test Person", "person@person.com", "12345678");

    @Test
    public void shouldReturnResponseWithErrorMessageWhenEmailHasBeenUsed() {
        when(repository.existsByEmail(anyString())).thenReturn(true);

        Response<?> response = service.create(personDTO);

        assertFalse(response.isSuccess());
        assertEquals("This email already been used", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    public void shouldReturnCreatedPersonWhenValidPersonDTOIsProvided() {
        when(repository.existsByEmail(anyString())).thenReturn(false);
        when(repository.save(any(Person.class))).thenReturn(person);

        Response<?> response = service.create(personDTO);

        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
    }

    @Test
    public void shouldReturnResponseWithPersonWhenPersonExistsById() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(person));

        Response<?> response = service.findById(1L);

        assertTrue(response.isSuccess());
        assertNotNull(response.getData());

        verify(repository, times(1)).findById(1L);
    }

    @Test
    public void shouldReturnResponseWithErrorMessageWhenPersonNotFound() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.empty());

        Response<?> response = service.findById(1L);

        assertFalse(response.isSuccess());
        assertEquals("Nonexistent person", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    public void shouldReturnPopulatedPersonDTOList() {
        when(repository.findAllSortedById(any(Pageable.class))).thenReturn(generatePersonList(10));

        Response<?> response = service.findAll(0, 10);

        assertTrue(response.isSuccess());
        assertFalse(((List<?>) response.getData()).isEmpty());
    }

    @Test
    public void shouldReturnEmptyPersonDTOList() {
        when(repository.findAllSortedById(any(Pageable.class))).thenReturn(new ArrayList<>());

        Response<?> response = service.findAll(0, 10);

        assertTrue(response.isSuccess());
        assertTrue(((List<?>) response.getData()).isEmpty());
    }

    @Test
    public void shouldReturnErrorMessageWhenNonExistentPersonToUpdate() {
        when(repository.existsById(anyLong())).thenReturn(false);

        Response<?> response = service.updateById(1L, personDTO);

        assertFalse(response.isSuccess());
        assertEquals("Nonexistent person", response.getMessage());
    }

    @Test
    public void shouldReturnErrorMessageWhenEmailExistsButDoesNotBelongToUser() {
        when(repository.existsById(anyLong())).thenReturn(true);
        when(repository.existsByEmail(anyString())).thenReturn(true);
        when(repository.isUserEmailByUserId(anyLong(), anyString())).thenReturn(false);

        Response<?> response = service.updateById(1L, personDTO);

        assertFalse(response.isSuccess());
        assertEquals("This email already been used", response.getMessage());
    }

    @Test
    public void shouldReturnSuccessResponseWhenPersonExistsAndEmailMatchesUser() {
        when(repository.existsById(anyLong())).thenReturn(true);
        when(repository.existsByEmail(anyString())).thenReturn(true);
        when(repository.isUserEmailByUserId(anyLong(), anyString())).thenReturn(true);
        when(repository.findById(anyLong())).thenReturn(Optional.of(person));

        Response<?> response = service.updateById(1L, personDTO);

        assertTrue(response.isSuccess());
    }

    @Test
    public void shouldReturnSuccessResponseWhenPersonExistsAndEmailIsValid() {
        when(repository.existsById(anyLong())).thenReturn(true);
        when(repository.existsByEmail(anyString())).thenReturn(false);
        when(repository.findById(anyLong())).thenReturn(Optional.of(person));

        Response<?> response = service.updateById(1L, personDTO);

        assertTrue(response.isSuccess());
    }

    @Test
    public void shouldDeletePerson() {
        service.deleteById(1L);

        verify(repository, times(1)).deleteById(anyLong());
    }

    public List<Person> generatePersonList(int amount) {
        List<Person> list = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            list.add(new Person((long) i, Integer.toString(i), i + "@email.com", "12345678"));
        }
        return list;
    }
}
