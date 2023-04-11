import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { OrderFormService, OrderFormGroup } from './order-form.service';
import { IOrder } from '../order.model';
import { OrderService } from '../service/order.service';
import { ICake } from 'app/entities/cake/cake.model';
import { CakeService } from 'app/entities/cake/service/cake.service';
import { Status } from 'app/entities/enumerations/status.model';

@Component({
  selector: 'jhi-order-update',
  templateUrl: './order-update.component.html',
})
export class OrderUpdateComponent implements OnInit {
  isSaving = false;
  order: IOrder | null = null;
  statusValues = Object.keys(Status);

  cakesSharedCollection: ICake[] = [];

  editForm: OrderFormGroup = this.orderFormService.createOrderFormGroup();

  constructor(
    protected orderService: OrderService,
    protected orderFormService: OrderFormService,
    protected cakeService: CakeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCake = (o1: ICake | null, o2: ICake | null): boolean => this.cakeService.compareCake(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ order }) => {
      this.order = order;
      if (order) {
        this.updateForm(order);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const order = this.orderFormService.getOrder(this.editForm);
    if (order.id !== null) {
      this.subscribeToSaveResponse(this.orderService.update(order));
    } else {
      this.subscribeToSaveResponse(this.orderService.create(order));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrder>>): void {
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

  protected updateForm(order: IOrder): void {
    this.order = order;
    this.orderFormService.resetForm(this.editForm, order);

    this.cakesSharedCollection = this.cakeService.addCakeToCollectionIfMissing<ICake>(this.cakesSharedCollection, order.cake);
  }

  protected loadRelationshipsOptions(): void {
    this.cakeService
      .query()
      .pipe(map((res: HttpResponse<ICake[]>) => res.body ?? []))
      .pipe(map((cakes: ICake[]) => this.cakeService.addCakeToCollectionIfMissing<ICake>(cakes, this.order?.cake)))
      .subscribe((cakes: ICake[]) => (this.cakesSharedCollection = cakes));
  }
}
