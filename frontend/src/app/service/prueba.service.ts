import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginService } from './login.service';
import { map, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PruebaService {
  
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
        
        return this.http.get<any[]>('http://localhost:8080/prueba', { headers });
        
      }
    
      addPrueba(prueba: any): Observable<any> {
        // Configurar el encabezado con el token de autorización
        const headers = new HttpHeaders({
          'Authorization': `Bearer ${this.token}`
        });
      
        // Hacer la solicitud POST al servidor con los datos del nuevo experto
        return this.http.post<any>('http://localhost:8080/prueba', prueba, { headers }).pipe(
          map((newPrueba) => {
            console.log('Nueva prueba agregada:', newPrueba);
            return newPrueba;  // Devuelve el experto recién creado
          })
        );
      }
      
    
      updatePrueba(prueba: any): Observable<any> {
        // Configurar el encabezado con el token de autorización
        const headers = new HttpHeaders({
          'Authorization': `Bearer ${this.token}`
        });
      
        // Hacer la solicitud PUT al servidor con los datos del experto
        return this.http.put<any>(`http://localhost:8080/prueba/${prueba.id}`, prueba, { headers }).pipe(
          map((updatedPrueba) => {
            console.log('Prueba actualizada:', updatedPrueba);
            return updatedPrueba;
          })
        );
      }
    
      
  
      deletePrueba(id: string): Observable<any> {
        const headers = new HttpHeaders({
          'Authorization': `Bearer ${this.token}`
        });
      
        return this.http.delete('http://localhost:8080/prueba/' + id, { headers });
      }
  
  
  
    
  
  
  
  
}
