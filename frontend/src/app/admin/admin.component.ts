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

  listAdmin : any[]=[]

  constructor(private admin : AdminServiceService){
    
    
  }
  ngOnInit(){
   
    this.admin.getAll().subscribe(res => {
      
      

        this.listAdmin= res;
      

      console.log(res);
      //como accedo a las propiedes

      
      
      
    });


    
    

    
   
   
 }



}
