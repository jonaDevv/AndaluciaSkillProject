import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ParticipantesServiceService {

  constructor(public http: HttpClient) { }





  getAll() : Observable<any>  {
    
   
  
   return  this.http.get('http://localhost:8080/participant')
      
  }

   // getAll(): Observable<any> {
  //   return this.http.get('https://restcountries.com/v3.1/all');
  // }

  
}

