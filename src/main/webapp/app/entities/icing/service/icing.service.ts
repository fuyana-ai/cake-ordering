import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIcing, NewIcing } from '../icing.model';

export type PartialUpdateIcing = Partial<IIcing> & Pick<IIcing, 'id'>;

export type EntityResponseType = HttpResponse<IIcing>;
export type EntityArrayResponseType = HttpResponse<IIcing[]>;

@Injectable({ providedIn: 'root' })
export class IcingService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/icings');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(icing: NewIcing): Observable<EntityResponseType> {
    return this.http.post<IIcing>(this.resourceUrl, icing, { observe: 'response' });
  }

  update(icing: IIcing): Observable<EntityResponseType> {
    return this.http.put<IIcing>(`${this.resourceUrl}/${this.getIcingIdentifier(icing)}`, icing, { observe: 'response' });
  }

  partialUpdate(icing: PartialUpdateIcing): Observable<EntityResponseType> {
    return this.http.patch<IIcing>(`${this.resourceUrl}/${this.getIcingIdentifier(icing)}`, icing, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIcing>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIcing[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getIcingIdentifier(icing: Pick<IIcing, 'id'>): number {
    return icing.id;
  }

  compareIcing(o1: Pick<IIcing, 'id'> | null, o2: Pick<IIcing, 'id'> | null): boolean {
    return o1 && o2 ? this.getIcingIdentifier(o1) === this.getIcingIdentifier(o2) : o1 === o2;
  }

  addIcingToCollectionIfMissing<Type extends Pick<IIcing, 'id'>>(
    icingCollection: Type[],
    ...icingsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const icings: Type[] = icingsToCheck.filter(isPresent);
    if (icings.length > 0) {
      const icingCollectionIdentifiers = icingCollection.map(icingItem => this.getIcingIdentifier(icingItem)!);
      const icingsToAdd = icings.filter(icingItem => {
        const icingIdentifier = this.getIcingIdentifier(icingItem);
        if (icingCollectionIdentifiers.includes(icingIdentifier)) {
          return false;
        }
        icingCollectionIdentifiers.push(icingIdentifier);
        return true;
      });
      return [...icingsToAdd, ...icingCollection];
    }
    return icingCollection;
  }
}
