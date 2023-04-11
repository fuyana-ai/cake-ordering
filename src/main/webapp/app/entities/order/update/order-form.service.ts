import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOrder, NewOrder } from '../order.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrder for edit and NewOrderFormGroupInput for create.
 */
type OrderFormGroupInput = IOrder | PartialWithRequiredKeyOf<NewOrder>;

type OrderFormDefaults = Pick<NewOrder, 'id'>;

type OrderFormGroupContent = {
  id: FormControl<IOrder['id'] | NewOrder['id']>;
  status: FormControl<IOrder['status']>;
  collectionDate: FormControl<IOrder['collectionDate']>;
  customerName: FormControl<IOrder['customerName']>;
  customerPhone: FormControl<IOrder['customerPhone']>;
  customerAdress: FormControl<IOrder['customerAdress']>;
  cake: FormControl<IOrder['cake']>;
};

export type OrderFormGroup = FormGroup<OrderFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OrderFormService {
  createOrderFormGroup(order: OrderFormGroupInput = { id: null }): OrderFormGroup {
    const orderRawValue = {
      ...this.getFormDefaults(),
      ...order,
    };
    return new FormGroup<OrderFormGroupContent>({
      id: new FormControl(
        { value: orderRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      status: new FormControl(orderRawValue.status),
      collectionDate: new FormControl(orderRawValue.collectionDate),
      customerName: new FormControl(orderRawValue.customerName),
      customerPhone: new FormControl(orderRawValue.customerPhone),
      customerAdress: new FormControl(orderRawValue.customerAdress),
      cake: new FormControl(orderRawValue.cake),
    });
  }

  getOrder(form: OrderFormGroup): IOrder | NewOrder {
    return form.getRawValue() as IOrder | NewOrder;
  }

  resetForm(form: OrderFormGroup, order: OrderFormGroupInput): void {
    const orderRawValue = { ...this.getFormDefaults(), ...order };
    form.reset(
      {
        ...orderRawValue,
        id: { value: orderRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): OrderFormDefaults {
    return {
      id: null,
    };
  }
}
