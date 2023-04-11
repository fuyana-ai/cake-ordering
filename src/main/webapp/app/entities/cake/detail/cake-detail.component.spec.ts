import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CakeDetailComponent } from './cake-detail.component';

describe('Cake Management Detail Component', () => {
  let comp: CakeDetailComponent;
  let fixture: ComponentFixture<CakeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CakeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ cake: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CakeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CakeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load cake on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.cake).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
