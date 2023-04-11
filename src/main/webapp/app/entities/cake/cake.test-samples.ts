import { Shape } from 'app/entities/enumerations/shape.model';
import { CakeSize } from 'app/entities/enumerations/cake-size.model';

import { ICake, NewCake } from './cake.model';

export const sampleWithRequiredData: ICake = {
  id: 16194,
};

export const sampleWithPartialData: ICake = {
  id: 39095,
  name: 'Automated Mississippi Montserrat',
};

export const sampleWithFullData: ICake = {
  id: 27776,
  name: 'transform Investor El',
  description: 'Virtual',
  price: 7580,
  shape: Shape['HEART'],
  cakeSize: CakeSize['MEDIUM'],
};

export const sampleWithNewData: NewCake = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
