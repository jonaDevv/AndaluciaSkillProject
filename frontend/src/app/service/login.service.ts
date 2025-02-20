// import { Injectable } from '@angular/core';
// import { HttpClient } from '@angular/common/http';
// import { map, Observable, throwError } from 'rxjs';

// @Injectable({
//   providedIn: 'root'
// })
// export class LoginService {

//   token : string;
//   perfil : string;
//   logeado : boolean;
//   usuario : any;

//   constructor(private http: HttpClient) {

//     this.token = "";
//     this.perfil = "";
//     this.logeado = false;
//     this.usuario = {};

//   }


//   private almacenar(){
//     var objeto:any;
//     objeto = {
//       token: this.token,
//       perfil: this.perfil,
//       logeado: this.logeado,
//       usuario: this.usuario
//     }
//     // Se guarda la informacion en el localStorage
//     sessionStorage.setItem("LOGIN", JSON.stringify(objeto));
//   }


//   recuperar(){
//     var cadena:string;
//     cadena = sessionStorage.getItem("LOGIN")||"";
//     if (cadena != ""){
      
//       // Se recupera la informacion del localStorage
//       var objeto:any = JSON.parse(cadena);

//       this.token = objeto.token;
//       this.perfil = objeto.perfil;
//       this.logeado = objeto.logeado;
//       this.usuario = objeto.usuario;

//     }else{

//       this.token = "";
//       this.perfil = "";
//       this.logeado = false;
//       this.usuario = {};
      
    
//     }
//   }

//   login(user:string, pass:string):Observable<any>{

//     let objeto:any;
//     objeto = this;

//     return this.http.post("http://localhost:8080/auth/login", {
//       username: user,
//       password: pass
//     })
//     .pipe(map((data:any)=>{
//       //Analizar respuesta
//       let respuesta:object={};
//       if(data!=null && data.token!=""){


//         objeto.usuario={"nombre":data.username}
//         objeto.perfil = data.roles[0].toLowerCase();
//         objeto.token = data.token;
//         objeto.logeado = true;
//         objeto.almacenar();

//         respuesta= {"funciona":true, "perfil":data.roles[0].toLowerCase()};

//       }else{
        
//         respuesta= {"funciona":false};
//       }

      
//       return respuesta;
      
//     }))



//   }

//   private machacar(){
//    sessionStorage.removeItem("LOGIN");
//   }


//   logout(){
//      let objeto:any=this;
//      let cont:any = sessionStorage.getItem("LOGIN");  
//     this.http.get("http://localhost/js/Angular/Servidor/login.php?desloguear="+
//      JSON.parse(cont||"").token)
//              .subscribe(function(data){

//                 objeto.machacar();

//               })
//   }


//   isLogged():boolean{

//     let respuesta:boolean=false;
//     let contenido:string|null = sessionStorage.getItem("LOGIN");  
    
//     if(contenido)
//     {
//       respuesta= JSON.parse(contenido||"").logeado;
//     }
//     return respuesta;
//   }


//   getNombre():string{
//     let respuesta:string="";
//     let contenido:string|null = sessionStorage.getItem("LOGIN");  
    
//     if(contenido)
//     {
//       respuesta= JSON.parse(contenido||"").usuario.nombre;
//     }
//     return respuesta;
//   }


//   getPerfil():string{
//     let respuesta:string="";
//     let contenido:string|null = sessionStorage.getItem("LOGIN");  
    
//     if(contenido)
//     {
//       console.log(JSON.parse(contenido||"").usuario.perfil)
//       respuesta= JSON.parse(contenido||"").perfil;
//     }
//     return respuesta;
//   }


//   getToken(){
//     let respuesta:string="";
//     let contenido:string|null = sessionStorage.getItem("LOGIN");  
    
//     if(contenido)
//     {
//       console.log(JSON.parse(contenido||"").token)
//       respuesta= JSON.parse(contenido||"").token;
//     }
//     return respuesta;
//   }






// }

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private authState = new BehaviorSubject<{ logeado: boolean, perfil: string, nombre: string }>({
    logeado: false,
    perfil: '',
    nombre: ''
  });

  token: string = '';
  perfil: string = '';
  logeado: boolean = false;
  usuario: any = {};

  constructor(private http: HttpClient) {
    this.recuperar(); // Recuperar el estado del login al inicializar el servicio
  }

  private almacenar() {
    const objeto = {
      token: this.token,
      perfil: this.perfil,
      logeado: this.logeado,
      usuario: this.usuario
    };
    sessionStorage.setItem("LOGIN", JSON.stringify(objeto));
    this.authState.next({ logeado: this.logeado, perfil: this.perfil, nombre: this.usuario.nombre });
  }

  recuperar() {
    const cadena = sessionStorage.getItem("LOGIN") || "";
    if (cadena) {
      const objeto = JSON.parse(cadena);
      this.token = objeto.token;
      this.perfil = objeto.perfil;
      this.logeado = objeto.logeado;
      this.usuario = objeto.usuario;
      this.authState.next({ logeado: this.logeado, perfil: this.perfil, nombre: this.usuario.nombre });
    }
  }

  login(user: string, pass: string): Observable<any> {
    return this.http.post("http://localhost:8080/auth/login", {
      username: user,
      password: pass
    }).pipe(
      map((data: any) => {
        if (data && data.token) {
          this.usuario = { nombre: data.username };
          this.perfil = data.roles[0].toLowerCase();
          this.token = data.token;
          this.logeado = true;
          this.almacenar();
        }
        return data;
      })
    );
  }

  logout() {
    this.token = '';
    this.perfil = '';
    this.logeado = false;
    this.usuario = {};
    sessionStorage.removeItem("LOGIN");
    this.authState.next({ logeado: false, perfil: '', nombre: '' });
  }

  getAuthState() {
    return this.authState.asObservable();
  }

  isLogged(): boolean {
    return this.logeado;
  }

  getNombre(): string {
    return this.usuario.nombre || '';
  }

  getPerfil(): string {
    return this.perfil || '';
  }

  getToken(): string {
    return this.token || '';
  }
}