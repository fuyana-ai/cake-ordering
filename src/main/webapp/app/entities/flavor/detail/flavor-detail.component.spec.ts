import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FlavorDetailComponent } from './flavor-detail.component';

describe('Flavor Management Detail Component', () => {
  let comp: FlavorDetailComponent;
  let fixture: ComponentFixture<FlavorDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FlavorDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ flavor: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FlavorDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FlavorDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load flavor on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.flavor).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
