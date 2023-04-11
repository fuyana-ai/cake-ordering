import { ICake } from 'app/entities/cake/cake.model';

export interface IColor {
  id: number;
  name?: string | null;
  cakes?: Pick<ICake, 'id'>[] | null;
}

export type NewColor = Omit<IColor, 'id'> & { id: null };
