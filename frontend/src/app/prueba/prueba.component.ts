import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SpecialtyService } from '../service/specialty.service';
import { FilterPipe } from '../filter.service';
import { PruebaService } from '../service/prueba.service';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-prueba',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, FilterPipe],
  templateUrl: './prueba.component.html',
  styleUrls: ['./prueba.component.css']
})
export class PruebaComponent implements OnInit {
  listPrueba: any[] = [];
  showPercentageError = false;
  prueba = { 
    id: '', 
    enunciado: '', 
    specialty: '', 
    specialtyName: '', 
    items: [] as any[], 
    pdfFile: null as File | null 
  };
  @ViewChild('pdfModal') pdfModal!: TemplateRef<any>;
  listSpecialty: any[] = [];
  searchText: string = '';
  errorMessage: string | null = null;
  isEditing = false;
  userEspecialidad: any;
  selectedPruebaId: any;
  safePdfUrl?: SafeResourceUrl;

  // Usamos una variable para almacenar la puntuación máxima
  maxScoreCalculada: number = 0;

  constructor(
    private sanitizer: DomSanitizer,
    private pru: PruebaService,
    private modalService: NgbModal,
    private spe: SpecialtyService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.loadPruebas();
    this.loadSpecialties();
  }

  private loadPruebas() {
    this.pru.getAll(this.userEspecialidad).subscribe({
      next: (res) => this.listPrueba = res,
      error: (err) => console.error('Error loading pruebas:', err)
    });
  }

  private loadSpecialties() {
    this.spe.getAll().subscribe({
      next: (res) => this.listSpecialty = res,
      error: (err) => console.error('Error loading specialties:', err)
    });
  }

  ordenarPor(campo: string) {
    this.listPrueba = [...this.listPrueba].sort((a, b) => 
      a[campo] > b[campo] ? 1 : a[campo] < b[campo] ? -1 : 0
    );
  }

  abrirFormulario(modalContent: TemplateRef<any>) {
    // Reiniciamos la prueba y la puntuación máxima
    this.prueba = { 
      id: '', 
      enunciado: '', 
      specialty: '', 
      specialtyName: '', 
      items: [], 
      pdfFile: null 
    };
    this.maxScoreCalculada = 0;
    this.showPercentageError = false;
    this.isEditing = false;
    this.errorMessage = null;
    this.modalService.open(modalContent, { centered: true });
  }

  // verPDF(pdfUrl: string) {
  //   if (!pdfUrl) {
  //     this.errorMessage = 'No hay PDF disponible para visualizar';
  //     return;
  //   }
    
  //   try {
  //     this.safePdfUrl = this.sanitizer.bypassSecurityTrustResourceUrl(pdfUrl);
  //     this.modalService.open(this.pdfModal, { 
  //       size: 'xl',
  //       modalDialogClass: 'pdf-viewer-modal'
  //     });
  //   } catch (error) {
  //     console.error('Error al cargar el PDF:', error);
  //     this.errorMessage = 'Error al visualizar el PDF';
  //   }
  // }

  verPDF(pdfUrl: string) {
  if (!pdfUrl) {
    this.errorMessage = 'No hay PDF disponible para visualizar';
    return;
  }

  try {
    const baseUrl = 'http://localhost:8080/files/'; // Cambia el puerto si es necesario
    const fullPdfUrl = baseUrl + pdfUrl;

    this.safePdfUrl = this.sanitizer.bypassSecurityTrustResourceUrl(fullPdfUrl);

    this.modalService.open(this.pdfModal, { 
      size: 'xl',
      modalDialogClass: 'pdf-viewer-modal'
    });
  } catch (error) {
    console.error('Error al cargar el PDF:', error);
    this.errorMessage = 'Error al visualizar el PDF';
  }
}

  agregarItem() {
    this.prueba.items.push({
      description: '',
      weight: 0,
      percentage: 0,
      pruebaId: this.prueba.id
    });
    this.onItemChange(); // Recalcula la puntuación al agregar un item
  }

  eliminarItem(index: number) {
    this.prueba.items.splice(index, 1);
    this.onItemChange(); // Recalcula la puntuación al eliminar un item
  }

  onFileSelected(event: any) {
    const file: File = event.target.files[0];
    if (file && file.type === 'application/pdf') {
      this.prueba.pdfFile = file;
    } else {
      this.errorMessage = 'Por favor seleccione un archivo PDF válido';
    }
  }

  // Calcula la puntuación máxima basado en los pesos de los items
  onItemChange() {
    setTimeout(() => {
      this.maxScoreCalculada = this.prueba.items
        .map(item => +item.weight || 0)
        .reduce((total, weight) => total + weight, 0);
      this.cdr.detectChanges();
    }, 0);
  }

  // Calcula el total de porcentajes
  get porcentajeTotal(): number {
    return this.prueba.items.reduce((total, item) => total + (+item.percentage || 0), 0);
  }

  private createFormData(): FormData {
    const formData = new FormData();
    formData.append('enunciado', this.prueba.enunciado);
    formData.append('maxScore', this.maxScoreCalculada.toString());
    formData.append('specialty', this.prueba.specialty);
    // formData.append('items', JSON.stringify(this.prueba.items));
    
     // Asegurarse de no enviar pruebaId en los items
    const sanitizedItems = this.prueba.items.map(item => {
      const { pruebaId, ...sanitizedItem } = item; // Eliminar pruebaId si está presente
      return sanitizedItem;
    });

    formData.append('items', JSON.stringify(sanitizedItems));
    if (this.prueba.pdfFile) {
      formData.append('pdfFile', this.prueba.pdfFile);
    }
    
    return formData;
  }

  guardarPrueba() {
    this.errorMessage = null;
   
  
    if (this.porcentajeTotal !== 100) {
      this.showPercentageError = true;
      this.errorMessage = `La suma de porcentajes debe ser 100% (Actual: ${this.porcentajeTotal}%)`;
      return;
    }
    const formData = this.createFormData();

    const serviceCall = this.isEditing 
      ? this.pru.updatePrueba(this.prueba.id, formData)
      : this.pru.addPrueba(formData);

    serviceCall.subscribe({
      next: (prueba) => {
        this.loadPruebas();
        this.modalService.dismissAll();
      },
      error: (err) => {
        console.error('Error:', err);
        this.errorMessage = err.error?.message || 'Error desconocido';
      }
    });
  }

  delete(id: string) {
    this.pru.deletePrueba(id).subscribe({
      next: () => this.loadPruebas(),
      error: (err) => {
        console.error('Error eliminando:', err);
        this.errorMessage = 'Error al eliminar la prueba';
      }
    });
  }

  editarPrueba(prue: any, modalContent: TemplateRef<any>) {
    this.prueba = { 
      ...prue,
      pdfFile: null,
      items: [...prue.items]
    };
    // Se recalcula la puntuación máxima al editar
    this.maxScoreCalculada = this.prueba.items
      .map(item => +item.weight || 0)
      .reduce((total, weight) => total + weight, 0);
    this.showPercentageError = false;
    this.isEditing = true;
    this.errorMessage = null;
    this.modalService.open(modalContent, { centered: true });
  }

  confirmDelete(id: string, modalContent: TemplateRef<any>) {
    this.selectedPruebaId = id;
    this.modalService.open(modalContent);
  }
}
