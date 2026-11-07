package com.quickbite.contract;

import com.quickbite.exception.DataAccessException;
import com.quickbite.exception.DuplicateRecordException;
import com.quickbite.exception.InvalidCredentialsException;
import com.quickbite.exception.RecordNotFoundException;
import com.quickbite.exception.ValidationException;
import com.quickbite.model.User;

import java.util.List;

/**
 * Repository interface for User persistence.
 * Demonstrates OOP Concept #5: Interfaces.
 */
public interface UserRepository {
    void add(User u) throws DuplicateRecordException, ValidationException, DataAccessException;
    List<User> getAll() throws DataAccessException;
    User getById(int id) throws RecordNotFoundException, DataAccessException;
    User findByUsername(String username) throws RecordNotFoundException, DataAccessException;
    User findByEmail(String email) throws RecordNotFoundException, DataAccessException;
    boolean existsByUsername(String username) throws DataAccessException;
    boolean existsByEmail(String email) throws DataAccessException;
    User authenticate(String usernameOrEmail, String password) throws InvalidCredentialsException, RecordNotFoundException, DataAccessException;
    List<User> search(String text) throws DataAccessException;
    int countAdmins() throws DataAccessException;
    void update(User updated) throws RecordNotFoundException, ValidationException, DataAccessException;
    void delete(int id) throws RecordNotFoundException, DataAccessException;
}
