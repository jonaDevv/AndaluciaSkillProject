import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { LoginService } from '../service/login.service';
export const guardExpertGuard: CanActivateFn = (route, state) => {
    
    let respuesta:boolean=false;

    let service= inject(LoginService);
    let ruta=inject(Router)

    if(service.getPerfil()=="EXPERT")
        respuesta=true
      else
      ruta.navigate(['login'])

    return respuesta;
};
