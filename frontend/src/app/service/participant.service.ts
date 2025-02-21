import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginService } from './login.service';
import { catchError, map, Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ParticipantService {
  
  token : any;
  header : any;
  constructor(private http: HttpClient, private loginser : LoginService) {

     this.token = loginser.getToken();

  }


  getAll(especialidad : any): Observable<any[]> {
    
    
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.token}`
    });
    
    return this.http.get<any[]>('http://localhost:8080/participant', { headers }).pipe(
      map((participantes: any[]) => {
       
        console.log(participantes.filter(participante => participante.specialtyName))
        console.log(especialidad)
        
        return participantes.filter(participante => participante.specialtyName === especialidad);
      }),
      catchError(error => {
        console.error('Error:', error);
        return of([]);
      })
    );
    
  }

  getAllP(): Observable<any[]> {
    
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.token}`
    });
    
    return this.http.get<any[]>('http://localhost:8080/participant', { headers }
    );
    
  }

  addParticipant(participante: any): Observable<any> {
    // Configurar el encabezado con el token de autorización
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.token}`
    });
  
    // Hacer la solicitud POST al servidor con los datos del nuevo experto
    return this.http.post<any>('http://localhost:8080/participant',participante, { headers }).pipe(
      map((newParticipant) => {
        console.log('Nuevo participante agregado:', newParticipant);
        return newParticipant;  // Devuelve el experto recién creado
      })
    );
  }
  

  updateParticipant(participante: any): Observable<any> {
    // Configurar el encabezado con el token de autorización
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.token}`
    });
  
    // Hacer la solicitud PUT al servidor con los datos del experto
    return this.http.put<any>(`http://localhost:8080/participant/${participante.id}`, participante, { headers }).pipe(
      map((updatedParticipant) => {
        console.log('Experto actualizado:', updatedParticipant);
        return updatedParticipant;
      })
    );
  }

  deleteParticipant(id: string): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.token}`
    });
  
    return this.http.delete('http://localhost:8080/participant/' + id, { headers });
  }
}



