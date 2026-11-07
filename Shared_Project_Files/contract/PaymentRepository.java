package com.quickbite.contract;

import com.quickbite.exception.DataAccessException;
import com.quickbite.exception.RecordNotFoundException;
import com.quickbite.exception.ValidationException;
import com.quickbite.model.Order;
import com.quickbite.model.Payment;

import java.util.List;

/**
 * Repository interface for Payment persistence.
 * Demonstrates OOP Concept #5: Interfaces.
 */
public interface PaymentRepository {
    void add(Payment p) throws ValidationException, DataAccessException;
    void addForOrder(Order o) throws ValidationException, DataAccessException;
    List<Payment> getAll() throws DataAccessException;
    Payment getById(int id) throws RecordNotFoundException, DataAccessException;
    Payment getByOrder(int orderId) throws RecordNotFoundException, DataAccessException;
    void update(Payment updated) throws RecordNotFoundException, ValidationException, DataAccessException;
    void onOrderStatus(int orderId, String orderStatus) throws DataAccessException;
    void deleteByOrder(int orderId) throws DataAccessException;
}
