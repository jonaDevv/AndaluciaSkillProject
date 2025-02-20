import { Routes } from '@angular/router';
import { ListaCompetidoresComponent } from './lista-competidores/lista-competidores.component';
import { LoginComponent } from './login/login.component';
import { AdminComponent } from './admin/admin.component';
import { ExpertComponent } from './expert/expert.component';
import { guardAdminGuard } from './autorizacion/guard-admin.guard';
import { guardExpertGuard } from './autorizacion/guard-expert.guard';
import { ListaParticipantesComponent } from './lista-participantes/lista-participantes.component';
import { EspecialidadComponent } from './especialidad/especialidad.component';

export const routes: Routes = [

    { path: '', component: ListaParticipantesComponent, pathMatch: 'full' },
    { path: 'login', component: LoginComponent, pathMatch: 'full' },
    { path: 'expert', component: ExpertComponent,canActivate: [guardExpertGuard], pathMatch: 'full',
        children: [
            { path: 'competidores', component: ListaParticipantesComponent, pathMatch: 'full' },
            { path: 'pruebas', component: ExpertComponent, pathMatch: 'full' },
            { path: 'evaluaciones', component: ExpertComponent, pathMatch: 'full' },
           
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
