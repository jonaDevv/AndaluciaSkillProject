import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginService } from './login.service';
import { catchError, map, Observable, of, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SpecialtyService {
  
  // URL base del backend
  private apiUrl = 'http://localhost:8080/specialty';

  constructor(private http: HttpClient, private loginser: LoginService) {}

  // Obtener el token actualizado y construir los headers
  private getHeaders(): HttpHeaders {
    const token = this.loginser.getToken();
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  // Manejo de errores
  private handleError(error: HttpErrorResponse): Observable<never> {
    console.error('Ocurrió un error:', error);
    // Aquí podrías, por ejemplo, notificar algo a nivel global,
    // pero si deseas que la cadena no se "rompa", podrías hacer algo como:
    // return of(null);  // Retorna un valor nulo (aunque esto depende de la lógica de la aplicación)
    return throwError(() => error);
  }

  // Obtener todas las especialidades
  getAll(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl, { headers: this.getHeaders() })
      .pipe(
        catchError(this.handleError)
      );
  }

  // Agregar una nueva especialidad
  addSpecialty(specialty: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, specialty, { headers: this.getHeaders() })
      .pipe(
        map((newSpecialty) => {
          console.log('Nueva especialidad agregada:', newSpecialty);
          return newSpecialty;
        }),
        catchError(this.handleError)
      );
  }

  // Actualizar una especialidad
  updateSpecialty(specialty: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${specialty.id}`, specialty, { headers: this.getHeaders() })
      .pipe(
        map((updatedSpecialty) => {
          console.log('Especialidad actualizada:', updatedSpecialty);
          return updatedSpecialty;
        }),
        catchError(this.handleError)
      );
  }

  // Eliminar una especialidad
  deleteSpecialty(id: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, { headers: this.getHeaders() })
      .pipe(
        catchError(this.handleError)
      );
  }
}
