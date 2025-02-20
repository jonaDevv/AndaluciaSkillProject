import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginService } from './login.service';
import { catchError, map, Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SpecialtyService {
  
  
  token : any;
  header : any;
  constructor(private http: HttpClient, private loginser : LoginService) {

     this.token = loginser.getToken();

  }


  getAll(): Observable<any[]> {
      console.log(this.token)
      const headers = new HttpHeaders({
        'Authorization': `Bearer ${this.token}`
      });
      
      return this.http.get<any[]>('http://localhost:8080/specialty', { headers });
      
    }
  
    addSpecialty(specialty: any): Observable<any> {
      // Configurar el encabezado con el token de autorización
      const headers = new HttpHeaders({
        'Authorization': `Bearer ${this.token}`
      });
    
      // Hacer la solicitud POST al servidor con los datos del nuevo experto
      return this.http.post<any>('http://localhost:8080/specialty', specialty, { headers }).pipe(
        map((newSpecialty) => {
          console.log('Nueva especialidad agregada:', newSpecialty);
          return newSpecialty;  // Devuelve el experto recién creado
        })
      );
    }
    
  
    updateSpecialty(specialty: any): Observable<any> {
      // Configurar el encabezado con el token de autorización
      const headers = new HttpHeaders({
        'Authorization': `Bearer ${this.token}`
      });
    
      // Hacer la solicitud PUT al servidor con los datos del experto
      return this.http.put<any>(`http://localhost:8080/specialty/${specialty.id}`, specialty, { headers }).pipe(
        map((updatedSpecialty) => {
          console.log('especialidad actualizado:', updatedSpecialty);
          return updatedSpecialty;
        })
      );
    }
  
    

    deleteSpecialty(id: string): Observable<any> {
      const headers = new HttpHeaders({
        'Authorization': `Bearer ${this.token}`
      });
    
      return this.http.delete('http://localhost:8080/specialty/' + id, { headers });
    }



  


}
