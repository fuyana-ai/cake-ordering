import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../cake.test-samples';

import { CakeFormService } from './cake-form.service';

describe('Cake Form Service', () => {
  let service: CakeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CakeFormService);
  });

  describe('Service methods', () => {
    describe('createCakeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCakeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            price: expect.any(Object),
            shape: expect.any(Object),
            cakeSize: expect.any(Object),
            flavors: expect.any(Object),
            colors: expect.any(Object),
            icing: expect.any(Object),
          })
        );
      });

      it('passing ICake should create a new form with FormGroup', () => {
        const formGroup = service.createCakeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            price: expect.any(Object),
            shape: expect.any(Object),
            cakeSize: expect.any(Object),
            flavors: expect.any(Object),
            colors: expect.any(Object),
            icing: expect.any(Object),
          })
        );
      });
    });

    describe('getCake', () => {
      it('should return NewCake for default Cake initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCakeFormGroup(sampleWithNewData);

        const cake = service.getCake(formGroup) as any;

        expect(cake).toMatchObject(sampleWithNewData);
      });

      it('should return NewCake for empty Cake initial value', () => {
        const formGroup = service.createCakeFormGroup();

        const cake = service.getCake(formGroup) as any;

        expect(cake).toMatchObject({});
      });

      it('should return ICake', () => {
        const formGroup = service.createCakeFormGroup(sampleWithRequiredData);

        const cake = service.getCake(formGroup) as any;

        expect(cake).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICake should not enable id FormControl', () => {
        const formGroup = service.createCakeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCake should disable id FormControl', () => {
        const formGroup = service.createCakeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
