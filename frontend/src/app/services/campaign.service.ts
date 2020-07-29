import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map, retry, mergeMap } from 'rxjs/operators';
import { Campaign, CampaignsResponse } from '../models/server_responses/campaign.model';

@Injectable({
  providedIn: 'root'
})
export class CampaignService {
  private url = '/api/campaigns';
  constructor(private http: HttpClient) { }

  getAllCampaigns(): Observable<Campaign[]> {
    const options = {
      responseType: 'json' as const
    };
    return this.http.get<CampaignsResponse>(this.url, options).pipe(
      map(res => res.campaigns)
    );
  }

  renameCampaign(campaign: Campaign): any {
    const body = {campaignId: campaign.campaignId.toString(), campaignName: campaign.campaignName};
    return this.http.put<any>(this.url, body)
    .pipe(retry(3), mergeMap(() => this.getAllCampaigns()));
  }
}
