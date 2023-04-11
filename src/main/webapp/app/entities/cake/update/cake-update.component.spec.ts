import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CakeFormService } from './cake-form.service';
import { CakeService } from '../service/cake.service';
import { ICake } from '../cake.model';
import { IFlavor } from 'app/entities/flavor/flavor.model';
import { FlavorService } from 'app/entities/flavor/service/flavor.service';
import { IColor } from 'app/entities/color/color.model';
import { ColorService } from 'app/entities/color/service/color.service';
import { IIcing } from 'app/entities/icing/icing.model';
import { IcingService } from 'app/entities/icing/service/icing.service';

import { CakeUpdateComponent } from './cake-update.component';

describe('Cake Management Update Component', () => {
  let comp: CakeUpdateComponent;
  let fixture: ComponentFixture<CakeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cakeFormService: CakeFormService;
  let cakeService: CakeService;
  let flavorService: FlavorService;
  let colorService: ColorService;
  let icingService: IcingService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CakeUpdateComponent],
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
      .overrideTemplate(CakeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CakeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cakeFormService = TestBed.inject(CakeFormService);
    cakeService = TestBed.inject(CakeService);
    flavorService = TestBed.inject(FlavorService);
    colorService = TestBed.inject(ColorService);
    icingService = TestBed.inject(IcingService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Flavor query and add missing value', () => {
      const cake: ICake = { id: 456 };
      const flavors: IFlavor[] = [{ id: 38468 }];
      cake.flavors = flavors;

      const flavorCollection: IFlavor[] = [{ id: 930 }];
      jest.spyOn(flavorService, 'query').mockReturnValue(of(new HttpResponse({ body: flavorCollection })));
      const additionalFlavors = [...flavors];
      const expectedCollection: IFlavor[] = [...additionalFlavors, ...flavorCollection];
      jest.spyOn(flavorService, 'addFlavorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cake });
      comp.ngOnInit();

      expect(flavorService.query).toHaveBeenCalled();
      expect(flavorService.addFlavorToCollectionIfMissing).toHaveBeenCalledWith(
        flavorCollection,
        ...additionalFlavors.map(expect.objectContaining)
      );
      expect(comp.flavorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Color query and add missing value', () => {
      const cake: ICake = { id: 456 };
      const colors: IColor[] = [{ id: 19576 }];
      cake.colors = colors;

      const colorCollection: IColor[] = [{ id: 52782 }];
      jest.spyOn(colorService, 'query').mockReturnValue(of(new HttpResponse({ body: colorCollection })));
      const additionalColors = [...colors];
      const expectedCollection: IColor[] = [...additionalColors, ...colorCollection];
      jest.spyOn(colorService, 'addColorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cake });
      comp.ngOnInit();

      expect(colorService.query).toHaveBeenCalled();
      expect(colorService.addColorToCollectionIfMissing).toHaveBeenCalledWith(
        colorCollection,
        ...additionalColors.map(expect.objectContaining)
      );
      expect(comp.colorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Icing query and add missing value', () => {
      const cake: ICake = { id: 456 };
      const icing: IIcing = { id: 39532 };
      cake.icing = icing;

      const icingCollection: IIcing[] = [{ id: 88825 }];
      jest.spyOn(icingService, 'query').mockReturnValue(of(new HttpResponse({ body: icingCollection })));
      const additionalIcings = [icing];
      const expectedCollection: IIcing[] = [...additionalIcings, ...icingCollection];
      jest.spyOn(icingService, 'addIcingToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cake });
      comp.ngOnInit();

      expect(icingService.query).toHaveBeenCalled();
      expect(icingService.addIcingToCollectionIfMissing).toHaveBeenCalledWith(
        icingCollection,
        ...additionalIcings.map(expect.objectContaining)
      );
      expect(comp.icingsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const cake: ICake = { id: 456 };
      const flavor: IFlavor = { id: 31441 };
      cake.flavors = [flavor];
      const color: IColor = { id: 37399 };
      cake.colors = [color];
      const icing: IIcing = { id: 89867 };
      cake.icing = icing;

      activatedRoute.data = of({ cake });
      comp.ngOnInit();

      expect(comp.flavorsSharedCollection).toContain(flavor);
      expect(comp.colorsSharedCollection).toContain(color);
      expect(comp.icingsSharedCollection).toContain(icing);
      expect(comp.cake).toEqual(cake);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICake>>();
      const cake = { id: 123 };
      jest.spyOn(cakeFormService, 'getCake').mockReturnValue(cake);
      jest.spyOn(cakeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cake });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cake }));
      saveSubject.complete();

      // THEN
      expect(cakeFormService.getCake).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cakeService.update).toHaveBeenCalledWith(expect.objectContaining(cake));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICake>>();
      const cake = { id: 123 };
      jest.spyOn(cakeFormService, 'getCake').mockReturnValue({ id: null });
      jest.spyOn(cakeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cake: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cake }));
      saveSubject.complete();

      // THEN
      expect(cakeFormService.getCake).toHaveBeenCalled();
      expect(cakeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICake>>();
      const cake = { id: 123 };
      jest.spyOn(cakeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cake });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cakeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFlavor', () => {
      it('Should forward to flavorService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(flavorService, 'compareFlavor');
        comp.compareFlavor(entity, entity2);
        expect(flavorService.compareFlavor).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareColor', () => {
      it('Should forward to colorService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(colorService, 'compareColor');
        comp.compareColor(entity, entity2);
        expect(colorService.compareColor).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareIcing', () => {
      it('Should forward to icingService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(icingService, 'compareIcing');
        comp.compareIcing(entity, entity2);
        expect(icingService.compareIcing).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
