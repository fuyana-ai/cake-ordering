import dayjs from 'dayjs/esm';

import { Status } from 'app/entities/enumerations/status.model';

import { IOrder, NewOrder } from './order.model';

export const sampleWithRequiredData: IOrder = {
  id: 47761,
};

export const sampleWithPartialData: IOrder = {
  id: 47091,
  collectionDate: dayjs('2023-04-11'),
  customerName: 'Handcrafted Account Ville',
  customerPhone: 'Wisconsin Officer',
};

export const sampleWithFullData: IOrder = {
  id: 12831,
  status: Status['DECLINED'],
  collectionDate: dayjs('2023-04-11'),
  customerName: 'Fantastic engine',
  customerPhone: 'solution-oriented Future',
  customerAdress: 'capacitor',
};

export const sampleWithNewData: NewOrder = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
