import { Component, OnInit, TemplateRef } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SpecialtyService } from '../service/specialty.service';
import { FilterPipe } from '../filter.service';

@Component({
  selector: 'app-expert',
  imports: [CommonModule, RouterModule, FormsModule, FilterPipe],
  templateUrl: './especialidad.component.html',
  styleUrls: ['./especialidad.component.css']
})
export class EspecialidadComponent implements OnInit {
  listSpecialty: any[] = [];
  specialty = { id: null, cod: '', name: '' };
  isEdit = false;  // Para saber si estamos editando una especialidad o agregando una nueva
  searchText: string = '';
  errorMessage: string | null = null; // Variable para manejar el mensaje de error
  selectedEspecialidadId:any;

  constructor(private spe: SpecialtyService, private modalService: NgbModal) {}

  ngOnInit() {
    this.spe.getAll().subscribe((res: any[]) => {
      this.listSpecialty = res;
    });
  }

  // Ordenar por el campo clicado
  ordenarPor(campo: string) {
    this.listSpecialty = this.listSpecialty.sort((a, b) => {
      if (a[campo] < b[campo]) {
        return -1;
      } else if (a[campo] > b[campo]) {
        return 1;
      }
      return 0;
    });
  }

  // Abrir el modal para agregar una nueva especialidad
  abrirFormulario(modalContent: TemplateRef<any>) {
    this.specialty = { id: null, cod: '', name: '' };  // Limpiar los campos
    this.isEdit = false;  // Se asume que es agregar una nueva especialidad
    this.errorMessage = null; // Limpiar el mensaje de error al abrir el modal
    this.modalService.open(modalContent);  // Abrimos el modal con el contenido
  }

  // Guardar especialidad
guardarEspecialidad() {
  this.errorMessage = null; // Limpiar el mensaje de error antes de intentar guardar

  if (this.isEdit) {
      this.spe.updateSpecialty(this.specialty).subscribe({
          next: (updated) => {
              if (updated) {
                  const index = this.listSpecialty.findIndex(es => es.id === updated.id);
                  if (index !== -1) {
                      this.listSpecialty[index] = updated;
                  }
                  this.modalService.dismissAll(); // Mover aquí el cierre del modal
              } else {
                  this.errorMessage = 'No se pudo actualizar la especialidad.';
              }
          },
          error: (err) => {
            console.log("Error al agregar la especialidad: ", err);
            // Manejar el error devuelto por el backend
            if (err.status === 409) { // Conflict (código o nombre ya existe)
                this.errorMessage = err.error.menssage; // Acceder al mensaje del backend
            } else {
                this.errorMessage = 'Error al agregar la especialidad.';
            }
          }
      });
  } else {
      this.spe.addSpecialty(this.specialty).subscribe({
          next: (newSpecialty) => {
            
              if (newSpecialty) {
                  
                  this.listSpecialty.push(newSpecialty);
                  this.modalService.dismissAll(); // Mover aquí el cierre del modal
              } else {
                  this.errorMessage = 'No se pudo agregar la nueva especialidad.';
              }
          },
          error: (err) => {

              console.log("Error al agregar la especialidad: ", err);
              // Manejar el error devuelto por el backend
              if (err.status === 409) { // Conflict (código o nombre ya existe)
                  this.errorMessage = err.error.menssage; // Acceder al mensaje del backend
              } else {
                  this.errorMessage = 'Error al agregar la especialidad.';
              }
          }
      });
  }
}

  // Eliminar una especialidad
  delete(id: string) {
    this.spe.deleteSpecialty(id).subscribe({
      next: () => {
        this.listSpecialty = this.listSpecialty.filter(spe => spe.id !== id);
      },
      error: (err) => {
        console.error('Error eliminando especialidad:', err);
        this.errorMessage = 'Error al eliminar la especialidad.';
      }
    });
  }

  // Editar una especialidad
  editarEspecialidad(especialidad: any, modalContent: TemplateRef<any>) {
    this.specialty = { ...especialidad };  // Copiar los datos de la especialidad seleccionada
    this.isEdit = true;
    this.errorMessage = null; // Limpiar el mensaje de error al abrir el modal
    this.modalService.open(modalContent);  // Abrimos el modal para editar
  }

  cerrarModal(modal: NgbModal) {
    this.modalService.dismissAll();
  }

    confirmDelete(id: any, modalContent: TemplateRef<any>) {
      this.selectedEspecialidadId = id;
      this.modalService.open(modalContent);
  }
}