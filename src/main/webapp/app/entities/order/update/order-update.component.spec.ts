import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OrderFormService } from './order-form.service';
import { OrderService } from '../service/order.service';
import { IOrder } from '../order.model';
import { ICake } from 'app/entities/cake/cake.model';
import { CakeService } from 'app/entities/cake/service/cake.service';

import { OrderUpdateComponent } from './order-update.component';

describe('Order Management Update Component', () => {
  let comp: OrderUpdateComponent;
  let fixture: ComponentFixture<OrderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let orderFormService: OrderFormService;
  let orderService: OrderService;
  let cakeService: CakeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OrderUpdateComponent],
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
      .overrideTemplate(OrderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orderFormService = TestBed.inject(OrderFormService);
    orderService = TestBed.inject(OrderService);
    cakeService = TestBed.inject(CakeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Cake query and add missing value', () => {
      const order: IOrder = { id: 456 };
      const cake: ICake = { id: 35457 };
      order.cake = cake;

      const cakeCollection: ICake[] = [{ id: 86005 }];
      jest.spyOn(cakeService, 'query').mockReturnValue(of(new HttpResponse({ body: cakeCollection })));
      const additionalCakes = [cake];
      const expectedCollection: ICake[] = [...additionalCakes, ...cakeCollection];
      jest.spyOn(cakeService, 'addCakeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(cakeService.query).toHaveBeenCalled();
      expect(cakeService.addCakeToCollectionIfMissing).toHaveBeenCalledWith(
        cakeCollection,
        ...additionalCakes.map(expect.objectContaining)
      );
      expect(comp.cakesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const order: IOrder = { id: 456 };
      const cake: ICake = { id: 43007 };
      order.cake = cake;

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(comp.cakesSharedCollection).toContain(cake);
      expect(comp.order).toEqual(order);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrder>>();
      const order = { id: 123 };
      jest.spyOn(orderFormService, 'getOrder').mockReturnValue(order);
      jest.spyOn(orderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ order });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: order }));
      saveSubject.complete();

      // THEN
      expect(orderFormService.getOrder).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(orderService.update).toHaveBeenCalledWith(expect.objectContaining(order));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrder>>();
      const order = { id: 123 };
      jest.spyOn(orderFormService, 'getOrder').mockReturnValue({ id: null });
      jest.spyOn(orderService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ order: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: order }));
      saveSubject.complete();

      // THEN
      expect(orderFormService.getOrder).toHaveBeenCalled();
      expect(orderService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrder>>();
      const order = { id: 123 };
      jest.spyOn(orderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ order });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orderService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCake', () => {
      it('Should forward to cakeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(cakeService, 'compareCake');
        comp.compareCake(entity, entity2);
        expect(cakeService.compareCake).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
