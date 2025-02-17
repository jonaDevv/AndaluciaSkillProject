import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { LoginService } from '../service/login.service';


export const guardAdminGuard: CanActivateFn = (route, state) => {
  let respuesta:boolean=false;
  
      let service= inject(LoginService);
      let ruta=inject(Router);

      if(service.getPerfil()=="ADMIN")
          respuesta=true
      else
      ruta.navigate(['login'])
  
      return respuesta;
};
