import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { catchError, map, Observable, of, throwError } from 'rxjs';
import { ExpertService } from '../service/expert.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-expert',
  imports: [CommonModule,RouterModule,FormsModule],
  templateUrl: './expert.component.html',
  styleUrl: './expert.component.css'
})
export class ExpertComponent {

  listExpert : any[]=[] 
  constructor(private admin : ExpertService) {

  }

  ngOnInit(){
   
    this.admin.getAll().subscribe((res: any[]) => {
      
      

        this.listExpert= res;
      

      console.log(res);
      
    });
   
 }


  delete(id : String){

    this.admin.deleteUser(id);
    this.listExpert.splice(this.listExpert.findIndex(expert => expert.id == id), 1);
  }


  
}
