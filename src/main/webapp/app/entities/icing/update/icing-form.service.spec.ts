import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../icing.test-samples';

import { IcingFormService } from './icing-form.service';

describe('Icing Form Service', () => {
  let service: IcingFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(IcingFormService);
  });

  describe('Service methods', () => {
    describe('createIcingFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createIcingFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          })
        );
      });

      it('passing IIcing should create a new form with FormGroup', () => {
        const formGroup = service.createIcingFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          })
        );
      });
    });

    describe('getIcing', () => {
      it('should return NewIcing for default Icing initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createIcingFormGroup(sampleWithNewData);

        const icing = service.getIcing(formGroup) as any;

        expect(icing).toMatchObject(sampleWithNewData);
      });

      it('should return NewIcing for empty Icing initial value', () => {
        const formGroup = service.createIcingFormGroup();

        const icing = service.getIcing(formGroup) as any;

        expect(icing).toMatchObject({});
      });

      it('should return IIcing', () => {
        const formGroup = service.createIcingFormGroup(sampleWithRequiredData);

        const icing = service.getIcing(formGroup) as any;

        expect(icing).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IIcing should not enable id FormControl', () => {
        const formGroup = service.createIcingFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewIcing should disable id FormControl', () => {
        const formGroup = service.createIcingFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
