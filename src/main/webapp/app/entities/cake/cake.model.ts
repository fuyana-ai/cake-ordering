import { IFlavor } from 'app/entities/flavor/flavor.model';
import { IColor } from 'app/entities/color/color.model';
import { IIcing } from 'app/entities/icing/icing.model';
import { Shape } from 'app/entities/enumerations/shape.model';
import { CakeSize } from 'app/entities/enumerations/cake-size.model';

export interface ICake {
  id: number;
  name?: string | null;
  description?: string | null;
  price?: number | null;
  shape?: Shape | null;
  cakeSize?: CakeSize | null;
  flavors?: Pick<IFlavor, 'id' | 'name'>[] | null;
  colors?: Pick<IColor, 'id' | 'name'>[] | null;
  icing?: Pick<IIcing, 'id'> | null;
}

export type NewCake = Omit<ICake, 'id'> & { id: null };
