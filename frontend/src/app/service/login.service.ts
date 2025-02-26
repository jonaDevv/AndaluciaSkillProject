

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private authState = new BehaviorSubject<{ logeado: boolean, perfil: string, nombre: string }>({
    logeado: false,
    perfil: '',
    nombre: '',
    
  });

  token: string = '';
  perfil: string = '';
  logeado: boolean = false;
  usuario: any = {};
  especialidad: string = '';

  constructor(private http: HttpClient) {
    this.recuperar(); // Recuperar el estado del login al inicializar el servicio
  }

  private almacenar() {
    const objeto = {
      token: this.token,
      perfil: this.perfil,
      logeado: this.logeado,
      usuario: this.usuario,
      especialidad: this.especialidad
    };
    sessionStorage.setItem("LOGIN", JSON.stringify(objeto));
    this.authState.next({ logeado: this.logeado, perfil: this.perfil, nombre: this.usuario.nombre });
  }

  recuperar() {
    const cadena = sessionStorage.getItem("LOGIN") || "";
    if (cadena) {
      const objeto = JSON.parse(cadena);
      this.token = objeto.token;
      this.perfil = objeto.perfil;
      this.logeado = objeto.logeado;
      this.especialidad = objeto.especialidad;
      this.usuario = objeto.usuario;
      this.authState.next({ logeado: this.logeado, perfil: this.perfil, nombre: this.usuario.nombre });
    }
  }

  login(user: string, pass: string): Observable<any> {
    return this.http.post("http://localhost:8080/auth/login", {
      username: user,
      password: pass
    }).pipe(
      map((data: any) => {
        console.log(data.specialtyName)
        if (data && data.token) {
          this.usuario = { nombre: data.username };
          this.perfil = data.roles[0].toLowerCase();
          this.especialidad = data.specialtyName;
          this.token = data.token;
          this.logeado = true;
          this.almacenar();
        }
        return data;
      })
    );
  }

  logout() {
    this.token = '';
    this.perfil = '';
    this.logeado = false;
    this.usuario = {};
    sessionStorage.removeItem("LOGIN");
    this.authState.next({ logeado: false, perfil: '', nombre: '' });
  }

  getAuthState() {
    return this.authState.asObservable();
  }

  isLogged(): boolean {
    return this.logeado;
  }

  getNombre(): string {
    return this.usuario.nombre || '';
  }

  getPerfil(): string {
    return this.perfil || '';
  }

  getToken(): string {


    return this.token || '';
  }

  getEspecialidad(): string {

    return this.especialidad || '';
  }
}