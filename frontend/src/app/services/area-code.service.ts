import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class AreaCodeService {

  constructor(private http: HttpClient) {
    this.getAreaCodes().subscribe(data => {
        console.log(data);
    });
}

public getAreaCodes(): Observable<any> {
    return this.http.get('./assets/data/AreaCodes.json');
}
}


