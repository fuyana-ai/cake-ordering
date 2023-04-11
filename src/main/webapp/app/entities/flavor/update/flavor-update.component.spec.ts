import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FlavorFormService } from './flavor-form.service';
import { FlavorService } from '../service/flavor.service';
import { IFlavor } from '../flavor.model';

import { FlavorUpdateComponent } from './flavor-update.component';

describe('Flavor Management Update Component', () => {
  let comp: FlavorUpdateComponent;
  let fixture: ComponentFixture<FlavorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let flavorFormService: FlavorFormService;
  let flavorService: FlavorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FlavorUpdateComponent],
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
      .overrideTemplate(FlavorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FlavorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    flavorFormService = TestBed.inject(FlavorFormService);
    flavorService = TestBed.inject(FlavorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const flavor: IFlavor = { id: 456 };

      activatedRoute.data = of({ flavor });
      comp.ngOnInit();

      expect(comp.flavor).toEqual(flavor);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFlavor>>();
      const flavor = { id: 123 };
      jest.spyOn(flavorFormService, 'getFlavor').mockReturnValue(flavor);
      jest.spyOn(flavorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ flavor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: flavor }));
      saveSubject.complete();

      // THEN
      expect(flavorFormService.getFlavor).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(flavorService.update).toHaveBeenCalledWith(expect.objectContaining(flavor));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFlavor>>();
      const flavor = { id: 123 };
      jest.spyOn(flavorFormService, 'getFlavor').mockReturnValue({ id: null });
      jest.spyOn(flavorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ flavor: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: flavor }));
      saveSubject.complete();

      // THEN
      expect(flavorFormService.getFlavor).toHaveBeenCalled();
      expect(flavorService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFlavor>>();
      const flavor = { id: 123 };
      jest.spyOn(flavorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ flavor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(flavorService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
