import { Component } from '@angular/core';
import { AdminServiceService } from '../service/admin.service.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-admin',
  imports: [FormsModule,RouterModule,CommonModule],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css'
})
export class AdminComponent {

  listExpert : any[]=[]

  constructor(private admin : AdminServiceService){
    
    
  }
  ngOnInit(){
   
    this.admin.getAll().subscribe(res => {
      
      

        this.listExpert= res;
      

      console.log(res);
      
    });
   
 }


  delete(id : String){

    this.admin.deleteUser(id);
    this.listExpert.splice(this.listExpert.findIndex(expert => expert.id == id), 1);
  }

  






}
