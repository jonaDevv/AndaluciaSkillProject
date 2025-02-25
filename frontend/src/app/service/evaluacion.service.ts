import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { LoginService } from './login.service';

@Injectable({ providedIn: 'root' })
export class EvaluacionService {
  private readonly apiUrl = 'http://localhost:8080/evaluaciones';
  
  constructor(
    private http: HttpClient,
    private loginService: LoginService
  ) {}

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Authorization': `Bearer ${this.loginService.getToken()}`
    });
  }

  getPendientes(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/pendientes`, {
      headers: this.getHeaders()
    }).pipe(
      tap(data => console.log('Datos recibidos:', data)),
      catchError(error => {
        console.error('Error en getPendientes:', error);
        return throwError(() => error);
      })
    );
  }

  getFinalizadas(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/finalizada`, {
      headers: this.getHeaders()
    }).pipe(
      tap(data => console.log('Datos recibidos:', data)),
      catchError(error => {
        console.error('Error en getFinalizada:', error);
        return throwError(() => error);
      })
    );
  }

  getDetails(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`, {
      headers: this.getHeaders()
    }).pipe(
      tap(data => console.log('Datos recibidos:', data)),
      catchError(error => {
        console.error('Error en getDetails:', error);
        return throwError(() => error);
      })
    );
  }

  calcular(id: number): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/${id}/calcular`, {}, {
      headers: this.getHeaders()
    });
  }

  finalizar(id: number): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/${id}/finalizar`, {}, {
      headers: this.getHeaders()
    });
  }

  updateItem(itemId: number, dto: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/items/${itemId}`, dto, {
      headers: this.getHeaders()
    });
  }
}
