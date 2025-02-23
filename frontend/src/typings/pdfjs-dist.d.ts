declare module 'pdfjs-dist/build/pdf.mjs' {
    import * as pdfjsLib from 'pdfjs-dist';
    export = pdfjsLib;
  }
  
  declare module 'pdfjs-dist/build/pdf.worker.mjs' {
    const workerSrc: string;
    export default workerSrc;
  }