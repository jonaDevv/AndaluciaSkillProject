import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { FilterPipe } from '../filter.service';
import { ParticipantService } from '../service/participant.service';

@Component({
  selector: 'app-ganadores',
  imports: [CommonModule, RouterModule, FormsModule,FilterPipe],
  templateUrl: './ganadores.component.html',
  styleUrl: './ganadores.component.css'
})
export class GanadoresComponent {

  ListaGanadores: any[] = [];
  searchText: string = '';
  participante = {  name: '', center: '', totalScore: '',specialtyName: '' };
  errorMessage: string | null = null; // Variable para manejar el mensaje de error

  constructor(private participantesService: ParticipantService) {
    
   
  }


  ngOnInit(): void {
    this.participantesService.getGanadores().subscribe(data => {
      this.ListaGanadores = data;
    });
  }


  // Ordenar por el campo clicado
  ordenarPor(campo: string) {
    this.ListaGanadores = this.ListaGanadores.sort((a, b) => {
      if (a[campo] < b[campo]) {
        return -1;
      } else if (a[campo] > b[campo]) {
        return 1;
      }
      return 0;
    });
  }
}
