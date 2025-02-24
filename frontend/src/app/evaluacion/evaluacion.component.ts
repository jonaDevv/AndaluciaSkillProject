// En app.module.ts
import { Component, NgModule, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';


import { EvaluacionService } from '../service/evaluacion.service';

import { CommonModule } from '@angular/common';
import { MatSliderModule } from '@angular/material/slider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';



@Component({
  selector: 'app-evaluaciones',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatSliderModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './evaluacion.component.html',
  styleUrls: ['./evaluacion.component.css']
})
export class EvaluacionComponent implements OnInit {
  evaluaciones: any[] = [];
  evaluacionSeleccionada: any = null;
  evaluacionForm: FormGroup;

  constructor(
    private evaluacionService: EvaluacionService,
    private fb: FormBuilder
  ) {
    this.evaluacionForm = this.fb.group({
      items: this.fb.array([])
    });
  }

  ngOnInit() {
    this.cargarEvaluaciones();
  }

  get itemsFormArray() {
    return this.evaluacionForm.get('items') as FormArray;
  }

  cargarEvaluaciones() {
    this.evaluacionService.getPendientes().subscribe({
      next: (data) => {
        console.log('Datos recibidos en el componente:', data);
        this.evaluaciones = data;
      },
      error: (err) => console.error('Error cargando evaluaciones:', err)
    });
  }

  seleccionarEvaluacion(evaluacionId: number) {
    this.evaluacionService.getDetails(evaluacionId).subscribe({
      next: (data) => {
        console.log('DETALLES DE EVALUACIÓN:', data); // ← Agrega este log
        this.evaluacionSeleccionada = data;
        this.inicializarFormulario(data.items);
      },
      error: (err) => console.error(err)
    });
  }

  // evaluacion.component.ts
  inicializarFormulario(items: any[]) {
    this.itemsFormArray.clear();
    
    items.forEach(item => {
      this.itemsFormArray.push(this.fb.group({
        id: [item.id],
        valoracion: [item.valoracion || 0, [Validators.required, Validators.min(0), Validators.max(100)]],
        justificacion: [item.justificacion || ''],
        itemId: [item.itemId], 
        description: [item.description] 
      }));
    });
    
    console.log('FORMULARIO INICIALIZADO:', this.itemsFormArray.value); // Debug
  }

  // Agregar para manejar posibles valores nulos
  get pruebaMaxScore(): number {
    return this.evaluacionSeleccionada?.pruebaMaxScore || 0;
  }

  guardarEvaluacion() {
    if (this.evaluacionForm.valid) {
      const updates = this.itemsFormArray.value.map((item: any) => 
        this.evaluacionService.updateItem(item.id, item)
      );

      Promise.all(updates).then(() => {
        this.evaluacionService.calcular(this.evaluacionSeleccionada.id).subscribe({
          next: () => this.actualizarLista()
        });
      });
    }
  }

  finalizarEvaluacion() {
    this.evaluacionService.finalizar(this.evaluacionSeleccionada.id).subscribe({
      next: () => this.actualizarLista()
    });
  }

  actualizarLista() {
    this.cargarEvaluaciones();
    this.cerrarModal();
  }

  cerrarModal() {
    this.evaluacionSeleccionada = null;
    this.evaluacionForm.reset();
  }
}