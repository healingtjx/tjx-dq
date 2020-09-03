package com.delayed.server.repository;

import com.delayed.server.model.DqTopicConfig;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

/**
 * @作者: tjx
 * @描述:
 * @创建时间: 创建于19:31 2020/7/15
 **/
@Component
public interface DqTopicConfigRepository extends CrudRepository<DqTopicConfig, Long> {
}
