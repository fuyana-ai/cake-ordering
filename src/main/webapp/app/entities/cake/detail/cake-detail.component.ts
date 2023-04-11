import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICake } from '../cake.model';

@Component({
  selector: 'jhi-cake-detail',
  templateUrl: './cake-detail.component.html',
})
export class CakeDetailComponent implements OnInit {
  cake: ICake | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cake }) => {
      this.cake = cake;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
