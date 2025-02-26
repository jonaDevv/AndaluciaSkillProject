import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { LoginService } from '../service/login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm: FormGroup;
  errorMessage: string | null = null;

  constructor(
    private login: LoginService, 
    private route: Router,
    private fb: FormBuilder
  ) {
    this.loginForm = this.fb.group({
      usuario: ['', [Validators.required, Validators.minLength(3)]],
      clave: ['', [Validators.required, Validators.minLength(5)]]
    });
  }

  logear(): void {
    this.errorMessage = null;
    
    if (this.loginForm.invalid) {
      this.markAllAsTouched();
      return;
    }

    const { usuario, clave } = this.loginForm.value;
    
    this.login.login(usuario, clave).subscribe({
      next: (v) => {
        if (v.token) {
          this.route.navigate(["/" + v.roles[0].toLowerCase()]);
        } else {
          this.errorMessage = "Error en la autenticación";
        }
      },
      error: () => this.errorMessage = "Usuario o contraseña incorrectos"
    });
  }

  private markAllAsTouched(): void {
    Object.values(this.loginForm.controls).forEach(control => {
      control.markAsTouched();
    });
  }

  // Getters para facilitar el acceso a los controles en la plantilla
  get usuario() {
    return this.loginForm.get('usuario');
  }

  get clave() {
    return this.loginForm.get('clave');
  }
}
