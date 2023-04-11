import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../flavor.test-samples';

import { FlavorFormService } from './flavor-form.service';

describe('Flavor Form Service', () => {
  let service: FlavorFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FlavorFormService);
  });

  describe('Service methods', () => {
    describe('createFlavorFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFlavorFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            cakes: expect.any(Object),
          })
        );
      });

      it('passing IFlavor should create a new form with FormGroup', () => {
        const formGroup = service.createFlavorFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            cakes: expect.any(Object),
          })
        );
      });
    });

    describe('getFlavor', () => {
      it('should return NewFlavor for default Flavor initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFlavorFormGroup(sampleWithNewData);

        const flavor = service.getFlavor(formGroup) as any;

        expect(flavor).toMatchObject(sampleWithNewData);
      });

      it('should return NewFlavor for empty Flavor initial value', () => {
        const formGroup = service.createFlavorFormGroup();

        const flavor = service.getFlavor(formGroup) as any;

        expect(flavor).toMatchObject({});
      });

      it('should return IFlavor', () => {
        const formGroup = service.createFlavorFormGroup(sampleWithRequiredData);

        const flavor = service.getFlavor(formGroup) as any;

        expect(flavor).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFlavor should not enable id FormControl', () => {
        const formGroup = service.createFlavorFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFlavor should disable id FormControl', () => {
        const formGroup = service.createFlavorFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
