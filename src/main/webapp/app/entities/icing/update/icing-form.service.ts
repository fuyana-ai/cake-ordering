import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IIcing, NewIcing } from '../icing.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IIcing for edit and NewIcingFormGroupInput for create.
 */
type IcingFormGroupInput = IIcing | PartialWithRequiredKeyOf<NewIcing>;

type IcingFormDefaults = Pick<NewIcing, 'id'>;

type IcingFormGroupContent = {
  id: FormControl<IIcing['id'] | NewIcing['id']>;
  name: FormControl<IIcing['name']>;
};

export type IcingFormGroup = FormGroup<IcingFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class IcingFormService {
  createIcingFormGroup(icing: IcingFormGroupInput = { id: null }): IcingFormGroup {
    const icingRawValue = {
      ...this.getFormDefaults(),
      ...icing,
    };
    return new FormGroup<IcingFormGroupContent>({
      id: new FormControl(
        { value: icingRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(icingRawValue.name),
    });
  }

  getIcing(form: IcingFormGroup): IIcing | NewIcing {
    return form.getRawValue() as IIcing | NewIcing;
  }

  resetForm(form: IcingFormGroup, icing: IcingFormGroupInput): void {
    const icingRawValue = { ...this.getFormDefaults(), ...icing };
    form.reset(
      {
        ...icingRawValue,
        id: { value: icingRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): IcingFormDefaults {
    return {
      id: null,
    };
  }
}
