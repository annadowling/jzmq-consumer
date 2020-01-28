package com.msc.spring.consumer.message;

import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<com.msc.spring.consumer.message.Message, Long> {
}
