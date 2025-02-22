import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginService } from './login.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PruebaService {
  private readonly API_URL = 'http://localhost:8080/prueba';

  constructor(
    private http: HttpClient,
    private loginService: LoginService
  ) {}

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Authorization': `Bearer ${this.loginService.getToken()}`
    });
  }

  getAll(especialidad?: string): Observable<any[]> {
    const params: any = {};
    if (especialidad) params.especialidad = especialidad;
    
    return this.http.get<any[]>(this.API_URL, {
      headers: this.getHeaders(),
      params
    });
  }

  addPrueba(formData: FormData): Observable<any> {
    return this.http.post(this.API_URL, formData, {
      headers: this.getHeaders()
    });
  }

  updatePrueba(id: string, formData: FormData): Observable<any> {
    return this.http.put(`${this.API_URL}/${id}`, formData, {
      headers: this.getHeaders()
    });
  }

  deletePrueba(id: string): Observable<any> {
    return this.http.delete(`${this.API_URL}/${id}`, {
      headers: this.getHeaders()
    });
  }
}