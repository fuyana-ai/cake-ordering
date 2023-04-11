import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFlavor } from '../flavor.model';

@Component({
  selector: 'jhi-flavor-detail',
  templateUrl: './flavor-detail.component.html',
})
export class FlavorDetailComponent implements OnInit {
  flavor: IFlavor | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ flavor }) => {
      this.flavor = flavor;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
