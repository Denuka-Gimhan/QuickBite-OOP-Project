package com.quickbite.contract;

import com.quickbite.exception.DataAccessException;
import com.quickbite.exception.RecordNotFoundException;
import com.quickbite.exception.ValidationException;
import com.quickbite.model.Food;

import java.util.List;

/**
 * Repository interface for Food persistence.
 * Demonstrates OOP Concept #5: Interfaces.
 */
public interface FoodRepository {
    void add(Food food) throws ValidationException, DataAccessException;
    List<Food> getAll() throws DataAccessException;
    Food getById(int id) throws RecordNotFoundException, DataAccessException;
    List<Food> search(String text, String category) throws DataAccessException;
    List<Food> search(String text, String category, String sort) throws DataAccessException;
    void update(Food updated) throws RecordNotFoundException, ValidationException, DataAccessException;
    void delete(int id) throws RecordNotFoundException, DataAccessException;
}
