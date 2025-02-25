import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { forkJoin } from 'rxjs';
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
  evaluacionesPendientes: any[] = [];
  evaluacionesFinalizadas: any[] = [];
  evaluacionSeleccionada: any = null;
  evaluacionForm: FormGroup;
  modoLectura: boolean = false; // Cuando es true, la evaluación se muestra solo para ver

  constructor(
    private evaluacionService: EvaluacionService,
    private fb: FormBuilder,
    private cd: ChangeDetectorRef
  ) {
    this.evaluacionForm = this.fb.group({
      items: this.fb.array([])
    });
  }

  ngOnInit() {
    this.cargarEvaluaciones();
    this.cargarEvaluacionesFinalizadas();
  }

  get itemsFormArray() {
    return this.evaluacionForm.get('items') as FormArray;
  }

  cargarEvaluaciones() {
    this.evaluacionService.getPendientes().subscribe({
      next: (data) => {
        console.log('Evaluaciones pendientes recibidas:', data);
        this.evaluacionesPendientes = data;
      },
      error: (err) => console.error('Error cargando evaluaciones pendientes:', err)
    });
  }

  cargarEvaluacionesFinalizadas() {
    this.evaluacionService.getFinalizadas().subscribe({
      next: (data) => {
        console.log('Evaluaciones finalizadas recibidas:', data);
        this.evaluacionesFinalizadas = data;
      },
      error: (err) => console.error('Error cargando evaluaciones finalizadas:', err)
    });
  }

  // Para modo edición (pendientes)
seleccionarEvaluacion(evaluacionId: number) {
  this.modoLectura = false;
  this.evaluacionService.getDetails(evaluacionId).subscribe({
    next: (data) => {
      console.log('DETALLES DE EVALUACIÓN:', data);
      this.evaluacionSeleccionada = data;
      this.inicializarFormulario(data.items);
      this.cd.detectChanges();
    },
    error: (err) => console.error(err)
  });
}

// Para modo sólo lectura (finalizadas)
verEvaluacion(evaluacionId: number) {
  this.modoLectura = true;
  this.evaluacionService.getDetails(evaluacionId).subscribe({
    next: (data) => {
      console.log('DETALLES DE EVALUACIÓN (modo lectura):', data);
      this.evaluacionSeleccionada = data;
      this.inicializarFormulario(data.items);
      this.cd.detectChanges();
    },
    error: (err) => console.error(err)
  });
}

  inicializarFormulario(items: any[]) {
    this.itemsFormArray.clear();
    
    items.forEach(item => {
      const grupo = this.fb.group({
        id: [item.id],
        valoracion: [{ value: item.valoracion ?? 0, disabled: this.modoLectura }, [Validators.required, Validators.min(0), Validators.max(100)]],
        justificacion: [{ value: item.justificacion || '', disabled: this.modoLectura }],
        itemId: [item.itemId],
        description: [item.description]
      });
      
      this.itemsFormArray.push(grupo);
    });
    
    console.log('FORMULARIO INICIALIZADO:', this.itemsFormArray.value);
  }

  guardarEvaluacion() {
    if (this.evaluacionForm.valid) {
      const updateObservables = this.itemsFormArray.value.map((item: any) =>
        this.evaluacionService.updateItem(item.id, item)
      );
      forkJoin(updateObservables).subscribe({
        next: () => {
          this.evaluacionService.calcular(this.evaluacionSeleccionada.id).subscribe({
            next: () => this.actualizarLista()
          });
        },
        error: (err) => console.error('Error actualizando ítems:', err)
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
    this.cargarEvaluacionesFinalizadas();
    this.cerrarModal();
  }

  cerrarModal() {
    this.evaluacionSeleccionada = null;
    this.evaluacionForm.reset();
    this.modoLectura = false;
  }
}
