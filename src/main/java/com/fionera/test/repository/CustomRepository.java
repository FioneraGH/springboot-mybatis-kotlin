package com.fionera.test.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Created by fionera on 16-10-11.
 */
@NoRepositoryBean
public interface CustomRepository<T,ID extends Serializable> extends JpaRepository<T,ID>,JpaSpecificationExecutor<T>{
    Page<T> findByAuto(T example, Pageable pageable);
}