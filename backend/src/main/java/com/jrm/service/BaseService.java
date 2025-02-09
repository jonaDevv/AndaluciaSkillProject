package com.jrm.service;

import java.util.List;



public interface BaseService<T, N> {

     List<T> findAll();
     T findById(N id);
     T save(T t);
     T update(N id, T t);
     void delete(N id);


   

   
}
