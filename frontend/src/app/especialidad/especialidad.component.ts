import { Component, OnInit, TemplateRef } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { FormsModule } from '@angular/forms'; // Para el [(ngModel)] del search
import { SpecialtyService } from '../service/specialty.service';
import { FilterPipe } from '../filter.service';

@Component({
  selector: 'app-expert',
  imports: [CommonModule, RouterModule, ReactiveFormsModule, FormsModule, FilterPipe],
  templateUrl: './especialidad.component.html',
  styleUrls: ['./especialidad.component.css']
})
export class EspecialidadComponent implements OnInit {
  listSpecialty: any[] = [];
  specialtyForm!: FormGroup; // Formulario reactivo para el modal
  isEdit = false;             // Indica si se está editando o agregando
  searchText: string = '';    // Para el campo de búsqueda
  errorMessage: string | null = null;
  selectedEspecialidadId: any; // Almacena el id cuando edites o elimines

  constructor(
    private spe: SpecialtyService,
    private modalService: NgbModal,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.spe.getAll().subscribe((res: any[]) => {
      this.listSpecialty = res;
    });
  }

  // Ordena la lista según el campo clicado
  ordenarPor(campo: string) {
    this.listSpecialty.sort((a, b) => {
      if (a[campo] < b[campo]) return -1;
      if (a[campo] > b[campo]) return 1;
      return 0;
    });
  }

  // Abre el modal para agregar una nueva especialidad
  abrirFormulario(modalContent: TemplateRef<any>) {
    // Creamos el formulario con validadores: requerido y mínimo 3 caracteres.
    this.specialtyForm = this.fb.group({
      cod: ['', [Validators.required, Validators.minLength(3)]],
      name: ['', [Validators.required, Validators.minLength(3)]]
    });
    this.isEdit = false;
    this.errorMessage = null;
    this.modalService.open(modalContent);
  }

  // Abre el modal para editar y precarga los datos en el formulario
  editarEspecialidad(especialidad: any, modalContent: TemplateRef<any>) {
    this.specialtyForm = this.fb.group({
      cod: [especialidad.cod, [Validators.required, Validators.minLength(3)]],
      name: [especialidad.name, [Validators.required, Validators.minLength(3)]]
    });
    this.isEdit = true;
    this.errorMessage = null;
    this.selectedEspecialidadId = especialidad.id; // Guardamos el id para la actualización
    this.modalService.open(modalContent);
  }

  // Se ejecuta al enviar el formulario
  guardarEspecialidad() {
    this.errorMessage = null;
    if (this.specialtyForm.invalid) {
      // Si es inválido, marcamos todos los controles como tocados para mostrar errores
      Object.values(this.specialtyForm.controls).forEach(control => control.markAsTouched());
      return;
    }
    
    const formValue = this.specialtyForm.value;

    if (this.isEdit) {
      // Actualización de la especialidad: agregamos el id
      const updatedSpecialty = { ...formValue, id: this.selectedEspecialidadId };
      this.spe.updateSpecialty(updatedSpecialty).subscribe({
        next: (updated) => {
          if (updated) {
            const index = this.listSpecialty.findIndex(es => es.id === updated.id);
            if (index !== -1) {
              this.listSpecialty[index] = updated;
            }
            this.modalService.dismissAll();
          } else {
            this.errorMessage = 'No se pudo actualizar la especialidad.';
          }
        },
        error: (err) => {
          this.errorMessage =
            err.status === 409 ? err.error.menssage : 'Error al actualizar la especialidad.';
        }
      });
    } else {
      // Agregar nueva especialidad
      this.spe.addSpecialty(formValue).subscribe({
        next: (newSpecialty) => {
          if (newSpecialty) {
            this.listSpecialty.push(newSpecialty);
            this.modalService.dismissAll();
          } else {
            this.errorMessage = 'No se pudo agregar la nueva especialidad.';
          }
        },
        error: (err) => {
          this.errorMessage =
            err.status === 409 ? err.error.menssage : 'Error al agregar la especialidad.';
        }
      });
    }
  }

  // Elimina una especialidad
  delete(id: string) {
    this.spe.deleteSpecialty(id).subscribe({
      next: () => {
        this.listSpecialty = this.listSpecialty.filter(spe => spe.id !== id);
      },
      error: (err) => {
        this.errorMessage = 'Error al eliminar la especialidad.';
      }
    });
  }

  // Abre el modal de confirmación para eliminar
  confirmDelete(id: any, modalContent: TemplateRef<any>) {
    this.selectedEspecialidadId = id;
    this.modalService.open(modalContent);
  }
}
