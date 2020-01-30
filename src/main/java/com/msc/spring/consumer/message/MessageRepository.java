package com.msc.spring.consumer.message;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface MessageRepository extends CrudRepository<com.msc.spring.consumer.message.Message, Long> {
}
