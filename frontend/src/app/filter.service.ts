import { Injectable } from '@angular/core';
import { Pipe, PipeTransform } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
@Pipe({
  name: 'filter'
})
export class FilterPipe implements PipeTransform {
  transform(items: any[], searchText: string): any[] {
    if (!items || !searchText) {
      return items;
    }

    return items.filter(item => {
      let searchField = '';

      if (item.name) {
        searchField = item.name;
      } else if (item.nombre) {
        searchField = item.nombre;
      }

      return searchField.toLowerCase().includes(searchText.toLowerCase());
    });
  }
}
