package com.delayed.base.repository;

import com.delayed.base.model.DqRedisConfig;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @作者: tjx
 * @描述:
 * @创建时间: 创建于19:31 2020/7/15
 **/
@Component
public interface DqRedisConfigRepository extends CrudRepository<DqRedisConfig, Long> {

    @Query(name="findByUrl",value ="select t from DqRedisConfig t where t.url=:url ")
    List<DqRedisConfig> findByUrl (@Param("url") String url);

}
