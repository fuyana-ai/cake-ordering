import { IFlavor, NewFlavor } from './flavor.model';

export const sampleWithRequiredData: IFlavor = {
  id: 41865,
};

export const sampleWithPartialData: IFlavor = {
  id: 76859,
};

export const sampleWithFullData: IFlavor = {
  id: 36762,
  name: 'Wooden system approach',
};

export const sampleWithNewData: NewFlavor = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
