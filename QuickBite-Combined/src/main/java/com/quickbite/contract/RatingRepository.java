package com.quickbite.contract;

import com.quickbite.exception.DataAccessException;
import com.quickbite.exception.DuplicateRecordException;
import com.quickbite.exception.RecordNotFoundException;
import com.quickbite.exception.ValidationException;
import com.quickbite.model.Rating;

import java.util.List;

/**
 * Repository interface for Rating persistence.
 * Demonstrates OOP Concept #5: Interfaces.
 */
public interface RatingRepository {
    void add(Rating r) throws DuplicateRecordException, ValidationException, DataAccessException;
    List<Rating> getAll() throws DataAccessException;
    Rating getByOrder(int orderId) throws RecordNotFoundException, DataAccessException;
    double getAverage() throws DataAccessException;
    void delete(int id) throws RecordNotFoundException, DataAccessException;
    void deleteByOrder(int orderId) throws DataAccessException;
}
