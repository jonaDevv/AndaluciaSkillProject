import { Component, OnInit, TemplateRef } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ExpertService } from '../service/expert.service';
import { SpecialtyService } from '../service/specialty.service';
import { FilterPipe } from '../filter.service';

@Component({
  selector: 'app-expert',
  imports: [CommonModule, RouterModule, FormsModule,FilterPipe],
  templateUrl: './expert.component.html',
  styleUrls: ['./expert.component.css']
})
export class ExpertComponent implements OnInit {
  listExpert: any[] = [];
  experto = {  dni: '', nombre: '', username: '', password: '', specialtyId: '',specialtyName: '' };
  listSpecialty: any[] = [];
  searchText: string = '';
  errorMessage: string | null = null; // Variable para manejar el mensaje de error
  isEditing = false;  // Para saber si estamos editando un experto o agregando uno nuevo
  
  constructor(private exper: ExpertService, private modalService: NgbModal, private spe: SpecialtyService) {}

  ngOnInit() {
    this.exper.getAll().subscribe((res: any[]) => {
      this.listExpert = res;
    });

    this.spe.getAll().subscribe((res: any[]) => {
      this.listSpecialty = res;
    });
  }

  // Ordenar por el campo clicado
  ordenarPor(campo: string) {
    this.listExpert = this.listExpert.sort((a, b) => {
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
    this.experto = {  dni: '', nombre: '', username: '',password: '', specialtyId: '',specialtyName: '' };  // Limpiar los campos
    this.isEditing = false;  // Se asume que es agregar un nuevo experto
    this.errorMessage = null; // Limpiar el mensaje de error al abrir el modal
    this.modalService.open(modalContent);  // Abrimos el modal con el contenido
  }

  // Guardar experto
  guardarExperto() {
  this.errorMessage = null; // Limpiar el mensaje de error antes de intentar guardar
    if (this.isEditing) {
      this.exper.updateUser(this.experto).subscribe({
        
        next: (updated) => {
          if (updated) {
              const index = this.listExpert.findIndex(ex => ex.id === updated.id);
              if (index !== -1) {
                  this.listExpert[index] = updated;
              }
              this.modalService.dismissAll(); // Mover aquí el cierre del modal
          } else {
              this.errorMessage = 'No se pudo actualizar el experto.';
          }
      },
        error: (err) => {
          console.log("Error al agregar el experto: ", err);
          // Manejar el error devuelto por el backend
          if (err.status === 409) { // Conflict (código o nombre ya existe)
              this.errorMessage = err.error.menssage; // Acceder al mensaje del backend
          } else {
              this.errorMessage = 'Error al agregar el experto.';
          }
        }
      });
    } else {
      console.log(this.experto)
      this.exper.addUser(this.experto).subscribe( {
          
          next: (newExpert) => {
              
            if (newExpert) {
                
                this.listExpert.push(newExpert);
                this.modalService.dismissAll(); // Mover aquí el cierre del modal
            } else {
                this.errorMessage = 'No se pudo agregar el nuevo experto.';
            }
        },
        error: (err) => {

            console.log("Error al agregar el experto: ", err);
            // Manejar el error devuelto por el backend
            if (err.status === 409) { // Conflict (código o nombre ya existe)
                this.errorMessage = err.error.menssage; // Acceder al mensaje del backend
            } else {
                this.errorMessage = 'Error al agregar el experto.';
            }
        }
        
      });
    }

    
    
  }

  // Eliminar un experto
  delete(id:any) {
    this.exper.deleteUser(id).subscribe({
      next: () => {
        this.listExpert = this.listExpert.filter(exp => exp.id !== id);
      },
      error: (err) => {
        console.error('Error eliminando experto:', err);
        this.errorMessage = 'Error al eliminar el experto.';
      }
    });
  }

  // Editar un experto
  editarExperto(expert: any, modalContent: TemplateRef<any>) {
    this.experto = { ...expert };  // Copiar los datos del experto seleccionado
    this.isEditing = true;
    this.errorMessage = null; // Limpiar el mensaje de error al abrir el modal
    this.modalService.open(modalContent);  // Abrimos el modal para editar
  }

  cerrarModal(modal: NgbModal) {
    this.modalService.dismissAll();
  }
}
