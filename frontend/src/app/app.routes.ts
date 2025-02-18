import { Routes } from '@angular/router';
import { ListaCompetidoresComponent } from './lista-competidores/lista-competidores.component';
import { LoginComponent } from './login/login.component';
import { AdminComponent } from './admin/admin.component';
import { ExpertComponent } from './expert/expert.component';
import { guardAdminGuard } from './autorizacion/guard-admin.guard';
import { guardExpertGuard } from './autorizacion/guard-expert.guard';

export const routes: Routes = [

    { path: '', component: ListaCompetidoresComponent, pathMatch: 'full' },
    { path: 'login', component: LoginComponent, pathMatch: 'full' },
    { path: 'EXPERT', component: ExpertComponent,canActivate: [guardExpertGuard], pathMatch: 'full' },
    { path: 'ADMIN', component: AdminComponent,canActivate: [guardAdminGuard], pathMatch: 'full' },
];
