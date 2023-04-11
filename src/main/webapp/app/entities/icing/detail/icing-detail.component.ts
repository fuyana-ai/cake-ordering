import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIcing } from '../icing.model';

@Component({
  selector: 'jhi-icing-detail',
  templateUrl: './icing-detail.component.html',
})
export class IcingDetailComponent implements OnInit {
  icing: IIcing | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ icing }) => {
      this.icing = icing;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
