import { Component } from '@angular/core';
import { CommonModule, NgFor } from '@angular/common';
import { Router } from '@angular/router';
import { LoginService } from '../service/login.service';

@Component({
  selector: 'app-navbar',
  imports: [ CommonModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

  
  perfil : string;
  nombre : string;
  
  constructor(private router: Router,private service: LoginService) {
  
    this.nombre = "";
    this.perfil = "";

  }

  ngOnInit(){
   
    this.nombre = this.getNombre();
    this.perfil = this.getPerfil();

  }

  login(){
    
    this.router.navigate(['/login']);
  }

  logout(){
    
    this.service.logout();
    this.nombre="";
    this.router.navigate(['/'])
   
  }

  isLogged(){
    
    return this.service.isLogged();
  }

  getNombre():string{
    
    return this.service.getNombre();
  }

  getPerfil():string{
    
    return this.service.getPerfil();
  }

}
