import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginService } from './login.service';
import { catchError, map, Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ExpertService {

  token : any;
  header : any;
  constructor(private http: HttpClient, private loginser : LoginService) {

     this.token = loginser.token;

  }


  // getAll(): Observable<any[]> {
  //   const loginData = sessionStorage.getItem("LOGIN") || '{}';
  //   const objeto = JSON.parse(loginData);
  
  //   if (!objeto?.token) {
  //     return throwError(() => new Error('Token no encontrado'));
  //   }
  
  //   const headers = new HttpHeaders({
  //     'Authorization': `Bearer ${objeto.token}`
  //   });
  
  //   return this.http.get<any[]>('http://localhost:8080/users', { headers }).pipe(
  //     map((usuarios: any[]) => {
  //       console.log(usuarios);
  //       return usuarios.filter(usuario => usuario.roles == 'EXPERT');
  //     }),
  //     catchError(error => {
  //       console.error('Error:', error);
  //       return of([]);
  //     })
  //   );
  // }

  getAll(): Observable<any[]> {

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.token}`
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
    
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.token}`
    });
    
    this.http.delete('http://localhost:8080/users/' + id, { headers })
             .subscribe(res => {
                          console.log(res);
    });
  }
}
