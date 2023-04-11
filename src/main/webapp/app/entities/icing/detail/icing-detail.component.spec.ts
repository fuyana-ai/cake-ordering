import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IcingDetailComponent } from './icing-detail.component';

describe('Icing Management Detail Component', () => {
  let comp: IcingDetailComponent;
  let fixture: ComponentFixture<IcingDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [IcingDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ icing: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(IcingDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(IcingDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load icing on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.icing).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
