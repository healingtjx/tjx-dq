package com.delayed.base.repository;

import com.delayed.base.model.DqTopicConfig;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: tjx
 * @描述:
 * @创建时间: 创建于19:31 2020/7/15
 **/
@Component
public interface DqTopicConfigRepository extends CrudRepository<DqTopicConfig, Long> {

    /**
     * 根据topic查询
     *
     * @param topic
     * @return
     */
    @Query("select t from DqTopicConfig t where t.topic = :topic  ")
    List<DqTopicConfig> findByTopic(@Param("topic") String topic);
}
