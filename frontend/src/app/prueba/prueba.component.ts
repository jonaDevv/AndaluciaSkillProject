import { Component, ElementRef, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SpecialtyService } from '../service/specialty.service';
import { FilterPipe } from '../filter.service';
import { PruebaService } from '../service/prueba.service';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { ChangeDetectorRef } from '@angular/core';
// import * as pdfjsLib from 'pdfjs-dist/build/pdf.mjs';
// import { getDocument } from 'pdfjs-dist/build/pdf.mjs';
import * as pdfjsLib from 'pdfjs-dist';
import { getDocument } from 'pdfjs-dist';


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
  @ViewChild('modalContent') modalContent!: TemplateRef<any>;
  @ViewChild('pdfCanvas', { static: false }) pdfCanvas!: ElementRef<HTMLCanvasElement>;
  listSpecialty: any[] = [];
  searchText: string = '';
  errorMessage: string | null = null;
  isEditing = false;
  userEspecialidad: any;
  selectedPruebaId: any;
  safePdfUrl?: SafeResourceUrl;
  page: number = 1;
  totalPages: number = 0;
  isLoaded: boolean = false;
  fullPdfUrl!: string;
  private pdfDoc: any = null;
  currentScale: number = 1.0;
  zoomStep: number = 0.25;


   currentPdfUrl!: string;
   reloadPdf = true;
   

 
    // Variables para arrastrar el PDF
    private isDragging = false;
    private offsetX = 0;
    private offsetY = 0;
    private startX = 0;
    private startY = 0;
    
    private boundMouseDown = (event: MouseEvent) => this.onMouseDown(event);
    private boundMouseMove = (event: MouseEvent) => this.onMouseMove(event);
    private boundMouseUp = () => this.onMouseUp();
  
  
 

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
    pdfjsLib.GlobalWorkerOptions.workerSrc = '/assets/pdf.worker.min.mjs';
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

  

 
  // Modifica el método verPDF para resetear la paginación
  async verPDF(pdfUrl: string) {
    
    if (!pdfUrl) {
      this.errorMessage = 'No hay PDF disponible para visualizar';
      return;
    }
    this.currentScale = 1.0;
    this.offsetX = 0;
    this.offsetY = 0;
  
    try {
      this.isLoaded = false;
      this.fullPdfUrl = `http://localhost:8080/files/${encodeURIComponent(pdfUrl)}`;
      
      const modalRef = this.modalService.open(this.pdfModal, { 
        size: 'xl',
        modalDialogClass: 'pdf-viewer-modal'
      });
  
      // Esperar a que el modal se renderice completamente
      await new Promise(resolve => setTimeout(resolve, 100));
      
      const loadingTask = getDocument({
        url: this.fullPdfUrl,
        disableAutoFetch: true
      });
      
      this.pdfDoc = await loadingTask.promise;
      this.totalPages = this.pdfDoc.numPages;
      this.page = 1;
      
      await this.renderPage(this.page);
  
    } catch (error) {
      this.handlePdfError();
    } finally {
      this.isLoaded = true;
      this.cdr.detectChanges();
    }
  }

  
 

  // Modificar el método downloadPdf
  async downloadPdf() {
    try {
      // Verificar que this.fullPdfUrl no sea undefined ni null
      if (!this.fullPdfUrl) {
        throw new Error('La URL del PDF no está definida.');
      }
  
      const response = await fetch(this.fullPdfUrl);
      if (!response.ok) throw new Error('Error en la descarga');
      
      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      
      const link = document.createElement('a');
      link.href = url;
  
      // Verificar que this.fullPdfUrl tiene un valor antes de usar split
      const fileName = this.fullPdfUrl.split('/').pop() || 'documento.pdf';
      link.download = fileName;
  
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);
      
    } catch (error) {
      console.error('Error descargando PDF:', error);
      this.errorMessage = 'Error al descargar el PDF';
    }
  }

  private async renderPage(pageNumber: number) {
    if (!this.pdfDoc) return;
    try {
      const page = await this.pdfDoc.getPage(pageNumber);
      const canvas = document.getElementById('pdfCanvas') as HTMLCanvasElement;
      const context = canvas.getContext('2d')!;
      
      // Obtener dimensiones del viewport escalado
      const scaledViewport = page.getViewport({ scale: this.currentScale });
      
      // Ajustar tamaño del canvas
      canvas.width = scaledViewport.width;
      canvas.height = scaledViewport.height;
      
      // Limpiar canvas
      context.clearRect(0, 0, canvas.width, canvas.height);
      
      // Renderizar la página con el nuevo zoom
      await page.render({
        canvasContext: context,
        viewport: scaledViewport
      }).promise;
      
    } catch (error) {
      console.error('Error renderizando página:', error);
      this.handlePdfError();
    }
  }
  
  
  private updateCanvasPosition() {
    const canvas = document.getElementById('pdfCanvas') as HTMLCanvasElement;
    canvas.style.transform = `translate(${this.offsetX}px, ${this.offsetY}px)`;
  }
  

  onMouseDown(event: MouseEvent) {
    event.stopPropagation();
    this.isDragging = true;
    this.startX = event.clientX - this.offsetX;
    this.startY = event.clientY - this.offsetY;
  }
  
  onMouseMove(event: MouseEvent) {
    event.stopPropagation();
    if (!this.isDragging) return;
    
    // Actualizar la posición del desplazamiento
    this.offsetX = event.clientX - this.startX;
    this.offsetY = event.clientY - this.startY;
    
    // Redibujar el canvas con el nuevo desplazamiento
    const canvas = this.pdfCanvas.nativeElement;
    canvas.style.transform = `translate(${this.offsetX}px, ${this.offsetY}px)`;
  }
  
  onMouseUp(event?: MouseEvent) {
    if (event) {
      event.stopPropagation();
    }
    this.isDragging = false;
    // Al finalizar, re-renderiza para fijar la nueva posición
    this.renderPage(this.page);
  }

  zoomIn() {
    this.currentScale = Math.min(3.0, this.currentScale + this.zoomStep);
    this.renderPage(this.page);
  }
  
  zoomOut() {
    this.currentScale = Math.max(0.5, this.currentScale - this.zoomStep);
    this.renderPage(this.page);
  }


  async previousPage() {
    if (this.page > 1) {
      this.page--;
      this.isLoaded = false;
      await this.renderPage(this.page);
      this.isLoaded = true;
    }
  }

  async nextPage() {
    if (this.page < this.totalPages) {
      this.page++;
      this.isLoaded = false;
      await this.renderPage(this.page);
      this.isLoaded = true;
    }
  }

  handlePdfError() {
    this.isLoaded = false;
    this.errorMessage = "Error al cargar el PDF";
    this.modalService.dismissAll();
  }


  private updateSafeUrl(page: number): void {
    this.reloadPdf = false; // Desactiva temporalmente el objeto
    const urlWithPage = `${this.fullPdfUrl}#page=${page}`;
    this.currentPdfUrl = urlWithPage;
    
    // Forzar actualización del objeto PDF
    setTimeout(() => {
      this.reloadPdf = true;
      this.safePdfUrl = this.sanitizer.bypassSecurityTrustResourceUrl(urlWithPage);
      this.cdr.detectChanges();
    }, 100);
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
