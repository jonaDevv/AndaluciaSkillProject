import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ParticipantesServiceService } from '../service/participantes-service.service';

@Component({
  selector: 'app-lista-participantes',
  imports: [FormsModule,RouterModule,CommonModule],
  templateUrl: './lista-participantes.component.html',
  styleUrl: './lista-participantes.component.css'
})
export class ListaParticipantesComponent {

  lParticipantes: any;

  constructor(private participant : ParticipantesServiceService) {

    
  };
  
  ngOnInit() {

    this.participant.getAll().subscribe(res => {
      
      
        this.lParticipantes= res;
      
      console.log(res);
      
    });
  }


}
