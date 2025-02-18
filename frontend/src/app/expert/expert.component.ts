import { Component, TemplateRef } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ExpertService } from '../service/expert.service';

@Component({
  selector: 'app-expert',
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './expert.component.html',
  styleUrls: ['./expert.component.css']
})
export class ExpertComponent {
  listExpert: any[] = [];
  experto = { id: null, dni: '', nombre: '', username: '', password: '', specialtyId: '' };
  isEditing = false;  // Para saber si estamos editando un experto o agregando uno nuevo

  constructor(private exper: ExpertService, private modalService: NgbModal) {}

  ngOnInit() {
    this.exper.getAll().subscribe((res: any[]) => {
      this.listExpert = res;
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
    this.experto = { id: null, dni: '', nombre: '', username: '',password: '', specialtyId:'' };  // Limpiar los campos
    this.isEditing = false;  // Se asume que es agregar un nuevo experto
    this.modalService.open(modalContent);  // Abrimos el modal con el contenido
  }

  // Guardar experto
  guardarExperto() {
    if (this.isEditing) {
      this.exper.updateUser(this.experto).subscribe(updated => {
        if (updated) {
          const index = this.listExpert.findIndex(ex => ex.id === updated.id);
          if (index !== -1) {
            this.listExpert[index] = updated;
          }
        } else {
          console.error('No se pudo actualizar el experto.');
        }
      });
    } else {
      this.exper.addUser(this.experto).subscribe(newExpert => {
        if (newExpert) {
          this.listExpert.push(newExpert);
        } else {
          console.error('No se pudo agregar el nuevo experto.');
        }
      });
    }

    this.modalService.dismissAll();
    
  }

  // Eliminar un experto
  delete(id: string) {
    this.exper.deleteUser(id).subscribe(() => {
      this.listExpert = this.listExpert.filter(expert => expert.id !== id);
    });
  }

  // Editar un experto
  editarExperto(expert: any, modalContent: TemplateRef<any>) {
    this.experto = { ...expert };  // Copiar los datos del experto seleccionado
    this.isEditing = true;
    this.modalService.open(modalContent);  // Abrimos el modal para editar
  }

  cerrarModal(modal: NgbModal) {
    this.modalService.dismissAll();
  }
}
