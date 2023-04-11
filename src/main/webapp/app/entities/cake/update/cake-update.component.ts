import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CakeFormService, CakeFormGroup } from './cake-form.service';
import { ICake } from '../cake.model';
import { CakeService } from '../service/cake.service';
import { IFlavor } from 'app/entities/flavor/flavor.model';
import { FlavorService } from 'app/entities/flavor/service/flavor.service';
import { IColor } from 'app/entities/color/color.model';
import { ColorService } from 'app/entities/color/service/color.service';
import { IIcing } from 'app/entities/icing/icing.model';
import { IcingService } from 'app/entities/icing/service/icing.service';
import { Shape } from 'app/entities/enumerations/shape.model';
import { CakeSize } from 'app/entities/enumerations/cake-size.model';

@Component({
  selector: 'jhi-cake-update',
  templateUrl: './cake-update.component.html',
})
export class CakeUpdateComponent implements OnInit {
  isSaving = false;
  cake: ICake | null = null;
  shapeValues = Object.keys(Shape);
  cakeSizeValues = Object.keys(CakeSize);

  flavorsSharedCollection: IFlavor[] = [];
  colorsSharedCollection: IColor[] = [];
  icingsSharedCollection: IIcing[] = [];

  editForm: CakeFormGroup = this.cakeFormService.createCakeFormGroup();

  constructor(
    protected cakeService: CakeService,
    protected cakeFormService: CakeFormService,
    protected flavorService: FlavorService,
    protected colorService: ColorService,
    protected icingService: IcingService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareFlavor = (o1: IFlavor | null, o2: IFlavor | null): boolean => this.flavorService.compareFlavor(o1, o2);

  compareColor = (o1: IColor | null, o2: IColor | null): boolean => this.colorService.compareColor(o1, o2);

  compareIcing = (o1: IIcing | null, o2: IIcing | null): boolean => this.icingService.compareIcing(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cake }) => {
      this.cake = cake;
      if (cake) {
        this.updateForm(cake);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cake = this.cakeFormService.getCake(this.editForm);
    if (cake.id !== null) {
      this.subscribeToSaveResponse(this.cakeService.update(cake));
    } else {
      this.subscribeToSaveResponse(this.cakeService.create(cake));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICake>>): void {
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

  protected updateForm(cake: ICake): void {
    this.cake = cake;
    this.cakeFormService.resetForm(this.editForm, cake);

    this.flavorsSharedCollection = this.flavorService.addFlavorToCollectionIfMissing<IFlavor>(
      this.flavorsSharedCollection,
      ...(cake.flavors ?? [])
    );
    this.colorsSharedCollection = this.colorService.addColorToCollectionIfMissing<IColor>(
      this.colorsSharedCollection,
      ...(cake.colors ?? [])
    );
    this.icingsSharedCollection = this.icingService.addIcingToCollectionIfMissing<IIcing>(this.icingsSharedCollection, cake.icing);
  }

  protected loadRelationshipsOptions(): void {
    this.flavorService
      .query()
      .pipe(map((res: HttpResponse<IFlavor[]>) => res.body ?? []))
      .pipe(map((flavors: IFlavor[]) => this.flavorService.addFlavorToCollectionIfMissing<IFlavor>(flavors, ...(this.cake?.flavors ?? []))))
      .subscribe((flavors: IFlavor[]) => (this.flavorsSharedCollection = flavors));

    this.colorService
      .query()
      .pipe(map((res: HttpResponse<IColor[]>) => res.body ?? []))
      .pipe(map((colors: IColor[]) => this.colorService.addColorToCollectionIfMissing<IColor>(colors, ...(this.cake?.colors ?? []))))
      .subscribe((colors: IColor[]) => (this.colorsSharedCollection = colors));

    this.icingService
      .query()
      .pipe(map((res: HttpResponse<IIcing[]>) => res.body ?? []))
      .pipe(map((icings: IIcing[]) => this.icingService.addIcingToCollectionIfMissing<IIcing>(icings, this.cake?.icing)))
      .subscribe((icings: IIcing[]) => (this.icingsSharedCollection = icings));
  }
}
