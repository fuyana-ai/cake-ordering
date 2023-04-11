import { ICake } from 'app/entities/cake/cake.model';

export interface IFlavor {
  id: number;
  name?: string | null;
  cakes?: Pick<ICake, 'id'>[] | null;
}

export type NewFlavor = Omit<IFlavor, 'id'> & { id: null };
