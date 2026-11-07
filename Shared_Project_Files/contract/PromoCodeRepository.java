package com.quickbite.contract;

import com.quickbite.exception.DataAccessException;
import com.quickbite.exception.DuplicateRecordException;
import com.quickbite.exception.RecordNotFoundException;
import com.quickbite.exception.ValidationException;
import com.quickbite.model.PromoCode;

import java.util.List;

/**
 * Repository interface for PromoCode persistence.
 * Demonstrates OOP Concept #5: Interfaces.
 */
public interface PromoCodeRepository {
    void add(PromoCode p) throws DuplicateRecordException, ValidationException, DataAccessException;
    List<PromoCode> getAll() throws DataAccessException;
    PromoCode findByCode(String code) throws RecordNotFoundException, DataAccessException;
    boolean existsByCode(String code) throws DataAccessException;
    void update(PromoCode updated) throws RecordNotFoundException, ValidationException, DataAccessException;
    void delete(String code) throws RecordNotFoundException, DataAccessException;
}
