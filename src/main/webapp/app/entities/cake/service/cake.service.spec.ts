import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICake } from '../cake.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../cake.test-samples';

import { CakeService } from './cake.service';

const requireRestSample: ICake = {
  ...sampleWithRequiredData,
};

describe('Cake Service', () => {
  let service: CakeService;
  let httpMock: HttpTestingController;
  let expectedResult: ICake | ICake[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CakeService);
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

    it('should create a Cake', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const cake = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(cake).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Cake', () => {
      const cake = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(cake).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Cake', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Cake', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Cake', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCakeToCollectionIfMissing', () => {
      it('should add a Cake to an empty array', () => {
        const cake: ICake = sampleWithRequiredData;
        expectedResult = service.addCakeToCollectionIfMissing([], cake);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cake);
      });

      it('should not add a Cake to an array that contains it', () => {
        const cake: ICake = sampleWithRequiredData;
        const cakeCollection: ICake[] = [
          {
            ...cake,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCakeToCollectionIfMissing(cakeCollection, cake);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Cake to an array that doesn't contain it", () => {
        const cake: ICake = sampleWithRequiredData;
        const cakeCollection: ICake[] = [sampleWithPartialData];
        expectedResult = service.addCakeToCollectionIfMissing(cakeCollection, cake);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cake);
      });

      it('should add only unique Cake to an array', () => {
        const cakeArray: ICake[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const cakeCollection: ICake[] = [sampleWithRequiredData];
        expectedResult = service.addCakeToCollectionIfMissing(cakeCollection, ...cakeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cake: ICake = sampleWithRequiredData;
        const cake2: ICake = sampleWithPartialData;
        expectedResult = service.addCakeToCollectionIfMissing([], cake, cake2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cake);
        expect(expectedResult).toContain(cake2);
      });

      it('should accept null and undefined values', () => {
        const cake: ICake = sampleWithRequiredData;
        expectedResult = service.addCakeToCollectionIfMissing([], null, cake, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cake);
      });

      it('should return initial array if no Cake is added', () => {
        const cakeCollection: ICake[] = [sampleWithRequiredData];
        expectedResult = service.addCakeToCollectionIfMissing(cakeCollection, undefined, null);
        expect(expectedResult).toEqual(cakeCollection);
      });
    });

    describe('compareCake', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCake(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCake(entity1, entity2);
        const compareResult2 = service.compareCake(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCake(entity1, entity2);
        const compareResult2 = service.compareCake(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCake(entity1, entity2);
        const compareResult2 = service.compareCake(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
