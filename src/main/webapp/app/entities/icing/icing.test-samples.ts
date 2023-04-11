import { IIcing, NewIcing } from './icing.model';

export const sampleWithRequiredData: IIcing = {
  id: 99695,
};

export const sampleWithPartialData: IIcing = {
  id: 71908,
  name: 'Director',
};

export const sampleWithFullData: IIcing = {
  id: 21407,
  name: 'Health',
};

export const sampleWithNewData: NewIcing = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
