package com.quickbite.contract;

import com.quickbite.exception.DataAccessException;
import com.quickbite.exception.RecordNotFoundException;
import com.quickbite.exception.ValidationException;
import com.quickbite.model.DeliveryPerson;

import java.util.List;

/**
 * Repository interface for DeliveryPerson (rider) persistence.
 * Demonstrates OOP Concept #5: Interfaces.
 */
public interface DeliveryPersonRepository {
    void add(DeliveryPerson d) throws ValidationException, DataAccessException;
    List<DeliveryPerson> getAll() throws DataAccessException;
    DeliveryPerson getById(int id) throws RecordNotFoundException, DataAccessException;
    List<DeliveryPerson> getAvailable() throws DataAccessException;
    void update(DeliveryPerson updated) throws RecordNotFoundException, ValidationException, DataAccessException;
    void delete(int id) throws RecordNotFoundException, DataAccessException;
}
