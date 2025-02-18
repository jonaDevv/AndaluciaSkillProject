import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LogoComponent } from "./logo/logo.component";
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NavbarComponent } from './navbar/navbar.component';
import { ListaCompetidoresComponent } from "./lista-competidores/lista-competidores.component";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, LogoComponent, CommonModule, FormsModule, NavbarComponent, ListaCompetidoresComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'competicion';
  nombre : string;

  constructor(){
    this.nombre = 'Mi webb';
  }
}
