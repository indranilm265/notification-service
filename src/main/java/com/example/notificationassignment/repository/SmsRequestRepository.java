package com.example.notificationassignment.repository;

import com.example.notificationassignment.models.SmsRequest;
import org.springframework.data.repository.CrudRepository;

public interface SmsRequestRepository extends CrudRepository<SmsRequest, Long> {
    boolean existsById(long id);
    SmsRequest getSmsRequestById(long id);
}
