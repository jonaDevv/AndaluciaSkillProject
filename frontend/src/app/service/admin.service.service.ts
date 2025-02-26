import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map,Observable, of, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdminServiceService {



  constructor(public http: HttpClient) {
    
    
  }

  // getAll() : Observable<any>  {
    
  //   var objeto:any = JSON.parse(sessionStorage.getItem("LOGIN")||"nanaina");
  //   console.log(objeto)
  //   console.log(objeto.token)
  //   // Configura los headers
  //   const headers = new HttpHeaders({
  //     'Authorization': `Bearer ${objeto.token}`
  //   });
  
  //  return  this.http.get('http://localhost:8080/users', { headers })
      
  // }

  getAll(): Observable<any[]> {
    const loginData = sessionStorage.getItem("LOGIN") || '{}';
    const objeto = JSON.parse(loginData);
  
    if (!objeto?.token) {
      return throwError(() => new Error('Token no encontrado'));
    }
  
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${objeto.token}`
    });
  
    return this.http.get<any[]>('http://localhost:8080/users', { headers }).pipe(
      map((usuarios: any[]) => {
        console.log(usuarios);
        return usuarios.filter(usuario => usuario.roles == 'EXPERT');
      }),
      catchError(error => {
        console.error('Error:', error);
        return of([]);
      })
    );
  }

  deleteUser(id : String):any{
    const loginData = sessionStorage.getItem("LOGIN") || '{}';
    const objeto = JSON.parse(loginData);
  
    if (!objeto?.token) {
      return throwError(() => new Error('Token no encontrado'));
    }
  
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${objeto.token}`
    });
    this.http.delete('http://localhost:8080/users/' + id, { headers })
             .subscribe(res => {
                          console.log(res);
    });
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
