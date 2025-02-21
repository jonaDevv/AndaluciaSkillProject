import { Routes } from '@angular/router';
import { ListaCompetidoresComponent } from './lista-competidores/lista-competidores.component';
import { LoginComponent } from './login/login.component';
import { AdminComponent } from './admin/admin.component';
import { ExpertComponent } from './expert/expert.component';
import { guardAdminGuard } from './autorizacion/guard-admin.guard';
import { guardExpertGuard } from './autorizacion/guard-expert.guard';
import { EspecialidadComponent } from './especialidad/especialidad.component';
import { ParticipantComponent } from './participant/participant.component';
import { PruebaComponent } from './prueba/prueba.component';

export const routes: Routes = [

    { path: '', component: ListaCompetidoresComponent, pathMatch: 'full' },
    { path: 'login', component: LoginComponent, pathMatch: 'full' },

    { path: 'expert', canActivate: [guardExpertGuard],
        children: [
            { path: '', redirectTo: 'participantes',pathMatch: 'full' },
            { path: 'participantes', component: ParticipantComponent, canActivate: [guardExpertGuard] },
            { path: 'pruebas', component: PruebaComponent, canActivate: [guardExpertGuard] },
            { path: 'evaluaciones', component: ExpertComponent,canActivate: [guardExpertGuard]},
           
        ]   
     },
    { path: 'admin', canActivate: [guardAdminGuard],
        children: [
            { path: '', redirectTo: 'expertos', pathMatch: 'full' },
            { path: 'expertos', component:ExpertComponent ,canActivate: [guardAdminGuard] },
            { path: 'ganadores', component: AdminComponent, canActivate: [guardAdminGuard],pathMatch: 'full' },
            { path: 'especialidad', component:EspecialidadComponent,canActivate: [guardAdminGuard], pathMatch: 'full' },
           
        ]
    },
    
];
