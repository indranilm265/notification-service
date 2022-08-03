package com.example.notificationassignment.repository;

import com.example.notificationassignment.models.BlacklistPhoneNumber;
import org.springframework.data.repository.CrudRepository;

public interface BlacklistRepository extends CrudRepository<BlacklistPhoneNumber, Long> {
    BlacklistPhoneNumber getBlacklistPhoneNumberByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumber(String phoneNumber);
    void deleteBlacklistPhoneNumberByPhoneNumber(String phoneNumber);
}
