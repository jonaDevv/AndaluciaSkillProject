package com.jrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jrm.model.Item;

public interface ItemRepository  extends JpaRepository<Item, Long> {

}
