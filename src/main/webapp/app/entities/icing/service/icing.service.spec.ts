import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IIcing } from '../icing.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../icing.test-samples';

import { IcingService } from './icing.service';

const requireRestSample: IIcing = {
  ...sampleWithRequiredData,
};

describe('Icing Service', () => {
  let service: IcingService;
  let httpMock: HttpTestingController;
  let expectedResult: IIcing | IIcing[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(IcingService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Icing', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const icing = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(icing).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Icing', () => {
      const icing = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(icing).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Icing', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Icing', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Icing', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addIcingToCollectionIfMissing', () => {
      it('should add a Icing to an empty array', () => {
        const icing: IIcing = sampleWithRequiredData;
        expectedResult = service.addIcingToCollectionIfMissing([], icing);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(icing);
      });

      it('should not add a Icing to an array that contains it', () => {
        const icing: IIcing = sampleWithRequiredData;
        const icingCollection: IIcing[] = [
          {
            ...icing,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addIcingToCollectionIfMissing(icingCollection, icing);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Icing to an array that doesn't contain it", () => {
        const icing: IIcing = sampleWithRequiredData;
        const icingCollection: IIcing[] = [sampleWithPartialData];
        expectedResult = service.addIcingToCollectionIfMissing(icingCollection, icing);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(icing);
      });

      it('should add only unique Icing to an array', () => {
        const icingArray: IIcing[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const icingCollection: IIcing[] = [sampleWithRequiredData];
        expectedResult = service.addIcingToCollectionIfMissing(icingCollection, ...icingArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const icing: IIcing = sampleWithRequiredData;
        const icing2: IIcing = sampleWithPartialData;
        expectedResult = service.addIcingToCollectionIfMissing([], icing, icing2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(icing);
        expect(expectedResult).toContain(icing2);
      });

      it('should accept null and undefined values', () => {
        const icing: IIcing = sampleWithRequiredData;
        expectedResult = service.addIcingToCollectionIfMissing([], null, icing, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(icing);
      });

      it('should return initial array if no Icing is added', () => {
        const icingCollection: IIcing[] = [sampleWithRequiredData];
        expectedResult = service.addIcingToCollectionIfMissing(icingCollection, undefined, null);
        expect(expectedResult).toEqual(icingCollection);
      });
    });

    describe('compareIcing', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareIcing(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareIcing(entity1, entity2);
        const compareResult2 = service.compareIcing(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareIcing(entity1, entity2);
        const compareResult2 = service.compareIcing(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareIcing(entity1, entity2);
        const compareResult2 = service.compareIcing(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
