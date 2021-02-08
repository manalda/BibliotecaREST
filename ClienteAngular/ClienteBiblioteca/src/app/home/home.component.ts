import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  goToCatalogue(): void {
    this.router.navigate(['catalogo'])
  }

  goToAddBook(): void {
    this.router.navigate(['add'])
  }

  goToSearchBook(): void {
    this.router.navigate(['busqueda'])
  }
}
