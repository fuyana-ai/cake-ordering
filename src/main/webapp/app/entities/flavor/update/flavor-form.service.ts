import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFlavor, NewFlavor } from '../flavor.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFlavor for edit and NewFlavorFormGroupInput for create.
 */
type FlavorFormGroupInput = IFlavor | PartialWithRequiredKeyOf<NewFlavor>;

type FlavorFormDefaults = Pick<NewFlavor, 'id' | 'cakes'>;

type FlavorFormGroupContent = {
  id: FormControl<IFlavor['id'] | NewFlavor['id']>;
  name: FormControl<IFlavor['name']>;
  cakes: FormControl<IFlavor['cakes']>;
};

export type FlavorFormGroup = FormGroup<FlavorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FlavorFormService {
  createFlavorFormGroup(flavor: FlavorFormGroupInput = { id: null }): FlavorFormGroup {
    const flavorRawValue = {
      ...this.getFormDefaults(),
      ...flavor,
    };
    return new FormGroup<FlavorFormGroupContent>({
      id: new FormControl(
        { value: flavorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(flavorRawValue.name),
      cakes: new FormControl(flavorRawValue.cakes ?? []),
    });
  }

  getFlavor(form: FlavorFormGroup): IFlavor | NewFlavor {
    return form.getRawValue() as IFlavor | NewFlavor;
  }

  resetForm(form: FlavorFormGroup, flavor: FlavorFormGroupInput): void {
    const flavorRawValue = { ...this.getFormDefaults(), ...flavor };
    form.reset(
      {
        ...flavorRawValue,
        id: { value: flavorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FlavorFormDefaults {
    return {
      id: null,
      cakes: [],
    };
  }
}
