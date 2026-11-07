package com.quickbite.contract;

import com.quickbite.exception.DataAccessException;
import com.quickbite.exception.RecordNotFoundException;
import com.quickbite.exception.ValidationException;
import com.quickbite.model.Order;

import java.util.List;

/**
 * Repository interface for Order persistence.
 * Demonstrates OOP Concept #5: Interfaces.
 */
public interface OrderRepository {
    void add(Order o) throws ValidationException, DataAccessException;
    List<Order> getAll() throws DataAccessException;
    Order getById(int id) throws RecordNotFoundException, DataAccessException;
    List<Order> getByUser(int userId) throws DataAccessException;
    void update(Order updated) throws RecordNotFoundException, ValidationException, DataAccessException;
    void delete(int id) throws RecordNotFoundException, DataAccessException;
}
