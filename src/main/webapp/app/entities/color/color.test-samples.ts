import { IColor, NewColor } from './color.model';

export const sampleWithRequiredData: IColor = {
  id: 43956,
};

export const sampleWithPartialData: IColor = {
  id: 67205,
  name: 'Qatari Bedfordshire',
};

export const sampleWithFullData: IColor = {
  id: 26320,
  name: 'Developer',
};

export const sampleWithNewData: NewColor = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
