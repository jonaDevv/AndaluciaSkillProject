// import { Component, OnInit } from '@angular/core';
// import { CommonModule, NgFor } from '@angular/common';
// import { Router, RouterModule } from '@angular/router';
// import { LoginService } from '../service/login.service';

// @Component({
//   selector: 'app-navbar',
//   imports: [ CommonModule,RouterModule],
//   templateUrl: './navbar.component.html',
//   styleUrl: './navbar.component.css'
// })
// export class NavbarComponent implements OnInit {

  
//   perfil : string;
//   nombre : string;
  
//   constructor(private router: Router,private service: LoginService) {
  
//     this.nombre = "";
//     this.perfil = "";

//   }

//   ngOnInit(){
   
//     this.nombre = this.getNombre();
//     this.perfil = this.getPerfil();

//   }

//   login(){
    
//     this.router.navigate(['/login']);
//   }

//   logout(){
    
//     this.service.logout();
//     this.nombre="";
//     this.router.navigate(['/'])
   
//   }

//   isLogged(){
    
//     return this.service.isLogged();
//   }

//   getNombre():string{
    
//     return this.service.getNombre();
//   }

//   getPerfil():string{
    
//     return this.service.getPerfil();
//   }

// }
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { LoginService } from '../service/login.service';

@Component({
  selector: 'app-navbar',
  standalone: true, // Asegúrate de que el componente sea standalone
  imports: [CommonModule, RouterModule], // Importa los módulos necesarios
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'] // Usa styleUrls en lugar de styleUrl
})
export class NavbarComponent implements OnInit {

  perfil: string = '';
  nombre: string = '';
  isLogged: boolean = false;

  constructor(private router: Router, private service: LoginService) {}

  ngOnInit() {
    // Suscribirse a los cambios en el estado de autenticación
    this.service.getAuthState().subscribe(state => {
      this.isLogged = state.logeado;
      this.perfil = state.perfil;
      this.nombre = state.nombre;
    });

    // Inicializar el estado actual
    this.isLogged = this.service.isLogged();
    this.nombre = this.service.getNombre();
    this.perfil = this.service.getPerfil();
  }

  login() {
    this.router.navigate(['/login']);
  }

  logout() {
    this.service.logout();
    this.router.navigate(['/']);
  }
}