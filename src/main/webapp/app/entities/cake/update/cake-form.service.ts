import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICake, NewCake } from '../cake.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICake for edit and NewCakeFormGroupInput for create.
 */
type CakeFormGroupInput = ICake | PartialWithRequiredKeyOf<NewCake>;

type CakeFormDefaults = Pick<NewCake, 'id' | 'flavors' | 'colors'>;

type CakeFormGroupContent = {
  id: FormControl<ICake['id'] | NewCake['id']>;
  name: FormControl<ICake['name']>;
  description: FormControl<ICake['description']>;
  price: FormControl<ICake['price']>;
  shape: FormControl<ICake['shape']>;
  cakeSize: FormControl<ICake['cakeSize']>;
  flavors: FormControl<ICake['flavors']>;
  colors: FormControl<ICake['colors']>;
  icing: FormControl<ICake['icing']>;
};

export type CakeFormGroup = FormGroup<CakeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CakeFormService {
  createCakeFormGroup(cake: CakeFormGroupInput = { id: null }): CakeFormGroup {
    const cakeRawValue = {
      ...this.getFormDefaults(),
      ...cake,
    };
    return new FormGroup<CakeFormGroupContent>({
      id: new FormControl(
        { value: cakeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(cakeRawValue.name),
      description: new FormControl(cakeRawValue.description),
      price: new FormControl(cakeRawValue.price),
      shape: new FormControl(cakeRawValue.shape),
      cakeSize: new FormControl(cakeRawValue.cakeSize),
      flavors: new FormControl(cakeRawValue.flavors ?? []),
      colors: new FormControl(cakeRawValue.colors ?? []),
      icing: new FormControl(cakeRawValue.icing),
    });
  }

  getCake(form: CakeFormGroup): ICake | NewCake {
    return form.getRawValue() as ICake | NewCake;
  }

  resetForm(form: CakeFormGroup, cake: CakeFormGroupInput): void {
    const cakeRawValue = { ...this.getFormDefaults(), ...cake };
    form.reset(
      {
        ...cakeRawValue,
        id: { value: cakeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CakeFormDefaults {
    return {
      id: null,
      flavors: [],
      colors: [],
    };
  }
}
