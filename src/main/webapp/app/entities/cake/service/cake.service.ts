import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICake, NewCake } from '../cake.model';

export type PartialUpdateCake = Partial<ICake> & Pick<ICake, 'id'>;

export type EntityResponseType = HttpResponse<ICake>;
export type EntityArrayResponseType = HttpResponse<ICake[]>;

@Injectable({ providedIn: 'root' })
export class CakeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cakes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cake: NewCake): Observable<EntityResponseType> {
    return this.http.post<ICake>(this.resourceUrl, cake, { observe: 'response' });
  }

  update(cake: ICake): Observable<EntityResponseType> {
    return this.http.put<ICake>(`${this.resourceUrl}/${this.getCakeIdentifier(cake)}`, cake, { observe: 'response' });
  }

  partialUpdate(cake: PartialUpdateCake): Observable<EntityResponseType> {
    return this.http.patch<ICake>(`${this.resourceUrl}/${this.getCakeIdentifier(cake)}`, cake, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICake>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICake[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCakeIdentifier(cake: Pick<ICake, 'id'>): number {
    return cake.id;
  }

  compareCake(o1: Pick<ICake, 'id'> | null, o2: Pick<ICake, 'id'> | null): boolean {
    return o1 && o2 ? this.getCakeIdentifier(o1) === this.getCakeIdentifier(o2) : o1 === o2;
  }

  addCakeToCollectionIfMissing<Type extends Pick<ICake, 'id'>>(
    cakeCollection: Type[],
    ...cakesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const cakes: Type[] = cakesToCheck.filter(isPresent);
    if (cakes.length > 0) {
      const cakeCollectionIdentifiers = cakeCollection.map(cakeItem => this.getCakeIdentifier(cakeItem)!);
      const cakesToAdd = cakes.filter(cakeItem => {
        const cakeIdentifier = this.getCakeIdentifier(cakeItem);
        if (cakeCollectionIdentifiers.includes(cakeIdentifier)) {
          return false;
        }
        cakeCollectionIdentifiers.push(cakeIdentifier);
        return true;
      });
      return [...cakesToAdd, ...cakeCollection];
    }
    return cakeCollection;
  }
}
