package com.jrm.service.base;

import java.util.List;
import java.util.Optional;





public interface BaseService<T, N> {

     List<T> findAll();
     Optional<T> findById(N id);
     T save(T user);
     T update(N id, T t);
     void delete(N id);


   

   
}
