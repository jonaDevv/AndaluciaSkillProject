import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-lista-competidores',
  imports: [CommonModule, FormsModule],
  templateUrl: './lista-competidores.component.html',
  styleUrl: './lista-competidores.component.css'
})
export class ListaCompetidoresComponent {

  listacompetidores:any[];
  constructor(){
    this.listacompetidores = [
      {
        id:1,
        nombre: "Jonatan",
        especialidad: "Informatica"
      },
      {
        id:2,
        nombre: "Juan",
        especialidad: "Ingeniería"
      },
      {
        id:3,
        nombre: "Pedro",
        especialidad: "Redes"
      },
      {
        id:4,
        nombre: "Carlos",
        especialidad: "Ingeniería"
      },
      {
        id:5,
        nombre: "Luis",
        especialidad: "Ingeniería"
      }
    ];
  } 

}
