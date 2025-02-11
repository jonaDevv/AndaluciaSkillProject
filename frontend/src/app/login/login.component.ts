import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { LoginService } from '../service/login.service';
import { RedirectCommand } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  usuario : string;
  clave : string;

  constructor(private login: LoginService) {
    this.usuario = "";
    this.clave = "";

  }

  logear():void{
    
    this.login.login(this.usuario, this.clave);

    
  }

}
