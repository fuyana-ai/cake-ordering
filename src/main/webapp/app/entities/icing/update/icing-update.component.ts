import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IcingFormService, IcingFormGroup } from './icing-form.service';
import { IIcing } from '../icing.model';
import { IcingService } from '../service/icing.service';

@Component({
  selector: 'jhi-icing-update',
  templateUrl: './icing-update.component.html',
})
export class IcingUpdateComponent implements OnInit {
  isSaving = false;
  icing: IIcing | null = null;

  editForm: IcingFormGroup = this.icingFormService.createIcingFormGroup();

  constructor(
    protected icingService: IcingService,
    protected icingFormService: IcingFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ icing }) => {
      this.icing = icing;
      if (icing) {
        this.updateForm(icing);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const icing = this.icingFormService.getIcing(this.editForm);
    if (icing.id !== null) {
      this.subscribeToSaveResponse(this.icingService.update(icing));
    } else {
      this.subscribeToSaveResponse(this.icingService.create(icing));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIcing>>): void {
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

  protected updateForm(icing: IIcing): void {
    this.icing = icing;
    this.icingFormService.resetForm(this.editForm, icing);
  }
}
