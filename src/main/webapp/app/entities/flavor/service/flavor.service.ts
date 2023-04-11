import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFlavor, NewFlavor } from '../flavor.model';

export type PartialUpdateFlavor = Partial<IFlavor> & Pick<IFlavor, 'id'>;

export type EntityResponseType = HttpResponse<IFlavor>;
export type EntityArrayResponseType = HttpResponse<IFlavor[]>;

@Injectable({ providedIn: 'root' })
export class FlavorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/flavors');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(flavor: NewFlavor): Observable<EntityResponseType> {
    return this.http.post<IFlavor>(this.resourceUrl, flavor, { observe: 'response' });
  }

  update(flavor: IFlavor): Observable<EntityResponseType> {
    return this.http.put<IFlavor>(`${this.resourceUrl}/${this.getFlavorIdentifier(flavor)}`, flavor, { observe: 'response' });
  }

  partialUpdate(flavor: PartialUpdateFlavor): Observable<EntityResponseType> {
    return this.http.patch<IFlavor>(`${this.resourceUrl}/${this.getFlavorIdentifier(flavor)}`, flavor, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFlavor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFlavor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFlavorIdentifier(flavor: Pick<IFlavor, 'id'>): number {
    return flavor.id;
  }

  compareFlavor(o1: Pick<IFlavor, 'id'> | null, o2: Pick<IFlavor, 'id'> | null): boolean {
    return o1 && o2 ? this.getFlavorIdentifier(o1) === this.getFlavorIdentifier(o2) : o1 === o2;
  }

  addFlavorToCollectionIfMissing<Type extends Pick<IFlavor, 'id'>>(
    flavorCollection: Type[],
    ...flavorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const flavors: Type[] = flavorsToCheck.filter(isPresent);
    if (flavors.length > 0) {
      const flavorCollectionIdentifiers = flavorCollection.map(flavorItem => this.getFlavorIdentifier(flavorItem)!);
      const flavorsToAdd = flavors.filter(flavorItem => {
        const flavorIdentifier = this.getFlavorIdentifier(flavorItem);
        if (flavorCollectionIdentifiers.includes(flavorIdentifier)) {
          return false;
        }
        flavorCollectionIdentifiers.push(flavorIdentifier);
        return true;
      });
      return [...flavorsToAdd, ...flavorCollection];
    }
    return flavorCollection;
  }
}
