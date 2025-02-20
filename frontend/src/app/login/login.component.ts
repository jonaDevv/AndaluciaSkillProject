import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { LoginService } from '../service/login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  usuario: string;
  clave: string;
  errorMessage: string | null = null; // Variable para manejar el mensaje de error

  constructor(private login: LoginService, private route: Router) {
    this.usuario = "";
    this.clave = "";
  }

  logear(): void {
    this.errorMessage = null; // Limpiar el mensaje de error antes de intentar el login

    this.login.login(this.usuario, this.clave).subscribe(
      (v) => {
        console.log(v);
        if (v.token) {
          this.route.navigate(["/" + v.roles[0].toLowerCase()]);
        } else {
          this.errorMessage = "Error en la autenticación"; // Mostrar mensaje de error
        }
      },
      (error) => {
        this.errorMessage = "Usuario o contraseña incorrectos"; // Mostrar mensaje de error en caso de fallo
      }
    );
  }
}