package com.vicctorb.wgtesttask36.repository;

import com.vicctorb.wgtesttask36.entities.CatsEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface CatsRepository extends JpaRepository<CatsEntity,String> {

    @Override
    List<CatsEntity> findAll(Sort sort);


    @Modifying
    @Transactional
    @Query(value = "insert into cats(name,color,tail_length,whiskers_length) values (?1,CAST(?2 AS cat_color),?3,?4)", nativeQuery = true)
    int saveCat(String name, String color, int tail_length, int whiskers_length);

}
