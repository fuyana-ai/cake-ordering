export interface IIcing {
  id: number;
  name?: string | null;
}

export type NewIcing = Omit<IIcing, 'id'> & { id: null };
