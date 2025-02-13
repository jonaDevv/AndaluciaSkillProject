import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdminServiceService {



  constructor(public http: HttpClient) {
    
    
  }

  getAll() : Observable<any>  {
    
    var objeto:any = JSON.parse(sessionStorage.getItem("LOGIN")||"nanaina");
    console.log(objeto)
    console.log(objeto.token)
    // Configura los headers
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${objeto.token}`
    });
  
   return  this.http.get('http://localhost:8080/users', { headers })
      
  }

  // getAll(): Observable<any> {
  //   return this.http.get('https://restcountries.com/v3.1/all');
  // }


  getName(name : String){
    this.http.get('https://restcountries.com/v3.1/name/' + name)
             .subscribe(res => {
                          console.log(res);
    });
  }

  getByCode(code : String) : Observable<any> {
    
    return this.http.get("https://restcountries.com/v3.1/alpha/" + code)
                    
  }
}
