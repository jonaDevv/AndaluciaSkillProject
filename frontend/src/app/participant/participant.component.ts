import { Component, OnInit, TemplateRef } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SpecialtyService } from '../service/specialty.service';
import { FilterPipe } from '../filter.service';
import { ParticipantService } from '../service/participant.service';
import { LoginService } from '../service/login.service';




@Component({
  selector: 'app-participant',
  imports: [CommonModule, RouterModule, FormsModule,FilterPipe],
  templateUrl: './participant.component.html',
  styleUrl: './participant.component.css'
})
export class ParticipantComponent implements OnInit {
  listParticipant: any[] = [];
  participante = {  name: '', center: '', totalScore: '', specialty: '',specialtyName: '' };
  listSpecialty: any[] = [];
  searchText: string = '';
  userEspecialidad: any;
  errorMessage: string | null = null; // Variable para manejar el mensaje de error
  isEditing = false;  // Para saber si estamos editando un experto o agregando uno nuevo
  selectedParticipantId: any;
  constructor(private p: ParticipantService, private modalService: NgbModal, private spe: SpecialtyService, private login: LoginService) {
    this.userEspecialidad = this.login.getEspecialidad();
  }

  ngOnInit() {
    this.p.getAll(this.userEspecialidad).subscribe((res: any[]) => {
      this.listParticipant = res;
    });

    this.spe.getAll().subscribe((res: any[]) => {
      this.listSpecialty = res;
    });
  }

  // Ordenar por el campo clicado
  ordenarPor(campo: string) {
    this.listParticipant = this.listParticipant.sort((a, b) => {
      if (a[campo] < b[campo]) {
        return -1;
      } else if (a[campo] > b[campo]) {
        return 1;
      }
      return 0;
    });
  }

  // Abrir el modal para agregar un nuevo experto
  abrirFormulario(modalContent: TemplateRef<any>) {
    this.participante = {  name: '', center: '', totalScore: '', specialty: '',specialtyName: '' };
    this.isEditing = false;  // Se asume que es agregar un nuevo experto
    this.errorMessage = null; // Limpiar el mensaje de error al abrir el modal
    this.modalService.open(modalContent);  // Abrimos el modal con el contenido
  }

  // Guardar experto
  guardarParticipante() {
  this.errorMessage = null; // Limpiar el mensaje de error antes de intentar guardar
    if (this.isEditing) {
      this.p.updateParticipant(this.participante).subscribe({
        
        next: (updated) => {
          if (updated) {
              const index = this.listParticipant.findIndex(pa => pa.id === updated.id);
              if (index !== -1) {
                  this.listParticipant[index] = updated;
              }
              this.modalService.dismissAll(); // Mover aquí el cierre del modal
          } else {
              this.errorMessage = 'No se pudo actualizar el participante.';
          }
      },
        error: (err) => {
          console.log("Error al agregar el participante: ", err);
          // Manejar el error devuelto por el backend
          if (err.status === 409) { // Conflict (código o nombre ya existe)
              this.errorMessage = err.error.menssage; // Acceder al mensaje del backend
          } else {
              this.errorMessage = 'Error al agregar el participante.';
          }
        }
      });
    } else {
      console.log(this.participante)
      this.p.addParticipant(this.participante).subscribe( {
          
          next: (newParticipant) => {
              
            if (newParticipant) {
                
                this.listParticipant.push(newParticipant);
                this.modalService.dismissAll(); // Mover aquí el cierre del modal
            } else {
                this.errorMessage = 'No se pudo agregar el nuevo participante.';
            }
        },
        error: (err) => {

            console.log("Error al agregar el participante: ", err);
            // Manejar el error devuelto por el backend
            if (err.status === 409) { // Conflict (código o nombre ya existe)
                this.errorMessage = err.error.menssage; // Acceder al mensaje del backend
            } else {
                this.errorMessage = 'Error al agregar el participante.';
            }
        }
        
      });
    }

    
    
  }

  // Eliminar un experto
  delete(id:any) {
    this.p.deleteParticipant(id).subscribe({
      next: () => {
        this.listParticipant = this.listParticipant.filter(par => par.id !== id);
      },
      error: (err) => {
        console.error('Error eliminando participante:', err);
        this.errorMessage = 'Error al eliminar participante.';
      }
    });
  }

  // Editar un experto
  editarParticipante(participant: any, modalContent: TemplateRef<any>) {
    this.participante = { ...participant };  // Copiar los datos del experto seleccionado
    this.isEditing = true;
    this.errorMessage = null; // Limpiar el mensaje de error al abrir el modal
    this.modalService.open(modalContent);  // Abrimos el modal para editar
  }

  cerrarModal(modal: NgbModal) {
    this.modalService.dismissAll();
  }

    confirmDelete(id: any, modalContent: TemplateRef<any>) {
      this.selectedParticipantId = id;
      this.modalService.open(modalContent);
  }

  
}
