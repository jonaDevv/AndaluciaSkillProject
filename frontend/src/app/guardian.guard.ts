import { ActivatedRouteSnapshot, CanActivateFn, GuardResult, MaybeAsync, Router, RouterStateSnapshot } from '@angular/router';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LoginService } from './service/login.service';
import {CanActivate} from '@angular/router';


@Injectable({
  providedIn: 'root'
})
export class guardianGuard implements CanActivate {

  constructor(private loginService: LoginService, private ruta: Router) {}
  
  
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    
    var respuesta:boolean = false;

    if(this.loginService.getPerfil() == "ADMIN"){
      respuesta =  true;

    }else{
      
      this.ruta.navigate(['/login']);
      respuesta = false;
    }


    return respuesta
    
   
  }

 
 
};
