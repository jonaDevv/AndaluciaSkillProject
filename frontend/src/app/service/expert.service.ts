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

     this.token = loginser.getToken();

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
    console.log(this.token)
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

  addUser(experto: any): Observable<any> {
    // Configurar el encabezado con el token de autorización
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.token}`
    });
  
    // Hacer la solicitud POST al servidor con los datos del nuevo experto
    return this.http.post<any>('http://localhost:8080/users', experto, { headers }).pipe(
      map((newExpert) => {
        console.log('Nuevo experto agregado:', newExpert);
        return newExpert;  // Devuelve el experto recién creado
      }),
      catchError(error => {
        console.error('Error al agregar el experto:', error);
        return of(null);  // Devuelve null si hay un error
      })
    );
  }
  

  updateUser(experto: any): Observable<any> {
    // Configurar el encabezado con el token de autorización
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.token}`
    });
  
    // Hacer la solicitud PUT al servidor con los datos del experto
    return this.http.put<any>(`http://localhost:8080/users/${experto.id}`, experto, { headers }).pipe(
      map((updatedExpert) => {
        console.log('Experto actualizado:', updatedExpert);
        return updatedExpert;
      }),
      catchError(error => {
        console.error('Error actualizando experto:', error);
        return of(null);  // Devuelve null si hay un error
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
