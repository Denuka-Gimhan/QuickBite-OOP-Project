package com.quickbite.contract;

import com.quickbite.exception.DataAccessException;
import com.quickbite.exception.RecordNotFoundException;
import com.quickbite.exception.ValidationException;
import com.quickbite.model.Delivery;

import java.util.List;

/**
 * Repository interface for Delivery persistence.
 * Demonstrates OOP Concept #5: Interfaces.
 */
public interface DeliveryRepository {
    void add(Delivery d) throws ValidationException, DataAccessException;
    List<Delivery> getAll() throws DataAccessException;
    Delivery getById(int id) throws RecordNotFoundException, DataAccessException;
    Delivery getByOrder(int orderId) throws RecordNotFoundException, DataAccessException;
    Delivery getActiveByOrder(int orderId) throws RecordNotFoundException, DataAccessException;
    boolean riderIsBusy(int personId) throws DataAccessException;
    void update(Delivery updated) throws RecordNotFoundException, ValidationException, DataAccessException;
    void delete(int id) throws RecordNotFoundException, DataAccessException;
    void deleteByOrder(int orderId) throws DataAccessException;
}
