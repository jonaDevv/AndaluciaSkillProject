import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { ExpertService } from '../service/expert.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
var bootstrap = require('bootstrap');
@Component({
  selector: 'app-expert',
  templateUrl: './expert.component.html',
  imports: [CommonModule,RouterModule,FormsModule],
  styleUrls: ['./expert.component.css']
})
export class ExpertComponent {
  listExpert: any[] = [];
  experto = { id: null, dni: '', nombre: '', username: '', especialidad: '' };
  isEditing = false;  // Para saber si estamos editando un experto o agregando uno nuevo

  constructor(private exper: ExpertService) {}

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
  abrirFormulario() {
    this.experto = { id: null, dni: '', nombre: '', username: '', especialidad: '' }; // Limpiar los campos
    this.isEditing = false;
    const modal = new bootstrap.Modal(document.getElementById('modalFormulario'));
    modal.show();
  }

  

  guardarExperto() {
    if (this.isEditing) {
      // Si estamos editando, actualizamos el experto
      this.exper.updateUser(this.experto).subscribe(updated => {
        if (updated) {
          // Reemplazamos el experto en la lista con el actualizado
          const index = this.listExpert.findIndex(ex => ex.id === updated.id);
          if (index !== -1) {
            this.listExpert[index] = updated;
          }
        } else {
          // Si hay un error, puedes mostrar un mensaje o manejarlo como prefieras
          console.error('No se pudo actualizar el experto.');
        }
      });
    } else {
      // Si no estamos editando, agregamos un nuevo experto
      this.exper.addUser(this.experto).subscribe(newExpert => {
        if (newExpert) {
          // Si el experto se agrega correctamente, lo añadimos a la lista
          this.listExpert.push(newExpert);
        } else {
          // Si hay un error al agregar, muestra un mensaje
          console.error('No se pudo agregar el nuevo experto.');
        }
      });
    }
    this.cerrarFormulario();  // Cierra el modal después de agregar o editar
  }
  

  // Cerrar el modal
  cerrarFormulario() {
    const modal = new bootstrap.Modal(document.getElementById('modalFormulario'));
    modal.hide();
  }

  // Eliminar un experto
  delete(id: string) {
    this.exper.deleteUser(id).subscribe(() => {
      this.listExpert = this.listExpert.filter(expert => expert.id !== id);
      console.log("Eliminado experto con ID:", id);
    });
  }

  // Editar un experto
  editarExperto(expert: any) {
    this.experto = { ...expert }; // Copiar los datos del experto seleccionado
    this.isEditing = true;
    const modal = new bootstrap.Modal(document.getElementById('modalFormulario'));
    modal.show();
  }
}
