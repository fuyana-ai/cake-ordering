import dayjs from 'dayjs/esm';
import { ICake } from 'app/entities/cake/cake.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IOrder {
  id: number;
  status?: Status | null;
  collectionDate?: dayjs.Dayjs | null;
  customerName?: string | null;
  customerPhone?: string | null;
  customerAdress?: string | null;
  cake?: Pick<ICake, 'id'> | null;
}

export type NewOrder = Omit<IOrder, 'id'> & { id: null };
