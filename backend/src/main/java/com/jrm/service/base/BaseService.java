package com.jrm.service.base;

import java.util.List;

import com.jrm.dto.user.UserCreateDTO;



public interface BaseService<T, N> {

     List<T> findAll();
     T findById(N id);
     T save(UserCreateDTO user);
     T update(N id, T t);
     void delete(N id);


   

   
}
