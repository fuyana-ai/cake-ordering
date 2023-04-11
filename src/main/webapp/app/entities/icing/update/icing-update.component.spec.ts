import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IcingFormService } from './icing-form.service';
import { IcingService } from '../service/icing.service';
import { IIcing } from '../icing.model';

import { IcingUpdateComponent } from './icing-update.component';

describe('Icing Management Update Component', () => {
  let comp: IcingUpdateComponent;
  let fixture: ComponentFixture<IcingUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let icingFormService: IcingFormService;
  let icingService: IcingService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [IcingUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(IcingUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IcingUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    icingFormService = TestBed.inject(IcingFormService);
    icingService = TestBed.inject(IcingService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const icing: IIcing = { id: 456 };

      activatedRoute.data = of({ icing });
      comp.ngOnInit();

      expect(comp.icing).toEqual(icing);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIcing>>();
      const icing = { id: 123 };
      jest.spyOn(icingFormService, 'getIcing').mockReturnValue(icing);
      jest.spyOn(icingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ icing });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: icing }));
      saveSubject.complete();

      // THEN
      expect(icingFormService.getIcing).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(icingService.update).toHaveBeenCalledWith(expect.objectContaining(icing));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIcing>>();
      const icing = { id: 123 };
      jest.spyOn(icingFormService, 'getIcing').mockReturnValue({ id: null });
      jest.spyOn(icingService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ icing: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: icing }));
      saveSubject.complete();

      // THEN
      expect(icingFormService.getIcing).toHaveBeenCalled();
      expect(icingService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIcing>>();
      const icing = { id: 123 };
      jest.spyOn(icingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ icing });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(icingService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
