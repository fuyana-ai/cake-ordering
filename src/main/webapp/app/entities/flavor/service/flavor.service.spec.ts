import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFlavor } from '../flavor.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../flavor.test-samples';

import { FlavorService } from './flavor.service';

const requireRestSample: IFlavor = {
  ...sampleWithRequiredData,
};

describe('Flavor Service', () => {
  let service: FlavorService;
  let httpMock: HttpTestingController;
  let expectedResult: IFlavor | IFlavor[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FlavorService);
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

    it('should create a Flavor', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const flavor = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(flavor).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Flavor', () => {
      const flavor = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(flavor).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Flavor', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Flavor', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Flavor', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFlavorToCollectionIfMissing', () => {
      it('should add a Flavor to an empty array', () => {
        const flavor: IFlavor = sampleWithRequiredData;
        expectedResult = service.addFlavorToCollectionIfMissing([], flavor);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(flavor);
      });

      it('should not add a Flavor to an array that contains it', () => {
        const flavor: IFlavor = sampleWithRequiredData;
        const flavorCollection: IFlavor[] = [
          {
            ...flavor,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFlavorToCollectionIfMissing(flavorCollection, flavor);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Flavor to an array that doesn't contain it", () => {
        const flavor: IFlavor = sampleWithRequiredData;
        const flavorCollection: IFlavor[] = [sampleWithPartialData];
        expectedResult = service.addFlavorToCollectionIfMissing(flavorCollection, flavor);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(flavor);
      });

      it('should add only unique Flavor to an array', () => {
        const flavorArray: IFlavor[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const flavorCollection: IFlavor[] = [sampleWithRequiredData];
        expectedResult = service.addFlavorToCollectionIfMissing(flavorCollection, ...flavorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const flavor: IFlavor = sampleWithRequiredData;
        const flavor2: IFlavor = sampleWithPartialData;
        expectedResult = service.addFlavorToCollectionIfMissing([], flavor, flavor2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(flavor);
        expect(expectedResult).toContain(flavor2);
      });

      it('should accept null and undefined values', () => {
        const flavor: IFlavor = sampleWithRequiredData;
        expectedResult = service.addFlavorToCollectionIfMissing([], null, flavor, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(flavor);
      });

      it('should return initial array if no Flavor is added', () => {
        const flavorCollection: IFlavor[] = [sampleWithRequiredData];
        expectedResult = service.addFlavorToCollectionIfMissing(flavorCollection, undefined, null);
        expect(expectedResult).toEqual(flavorCollection);
      });
    });

    describe('compareFlavor', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFlavor(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFlavor(entity1, entity2);
        const compareResult2 = service.compareFlavor(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFlavor(entity1, entity2);
        const compareResult2 = service.compareFlavor(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFlavor(entity1, entity2);
        const compareResult2 = service.compareFlavor(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
