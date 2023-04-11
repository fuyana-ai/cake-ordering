import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { FlavorFormService, FlavorFormGroup } from './flavor-form.service';
import { IFlavor } from '../flavor.model';
import { FlavorService } from '../service/flavor.service';

@Component({
  selector: 'jhi-flavor-update',
  templateUrl: './flavor-update.component.html',
})
export class FlavorUpdateComponent implements OnInit {
  isSaving = false;
  flavor: IFlavor | null = null;

  editForm: FlavorFormGroup = this.flavorFormService.createFlavorFormGroup();

  constructor(
    protected flavorService: FlavorService,
    protected flavorFormService: FlavorFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ flavor }) => {
      this.flavor = flavor;
      if (flavor) {
        this.updateForm(flavor);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const flavor = this.flavorFormService.getFlavor(this.editForm);
    if (flavor.id !== null) {
      this.subscribeToSaveResponse(this.flavorService.update(flavor));
    } else {
      this.subscribeToSaveResponse(this.flavorService.create(flavor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFlavor>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(flavor: IFlavor): void {
    this.flavor = flavor;
    this.flavorFormService.resetForm(this.editForm, flavor);
  }
}
