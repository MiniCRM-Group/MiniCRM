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

  getAllCampaigns(): Observable<CampaignsResponse> {
    const options = {
      responseType: 'json' as const
    };
    return this.http.get<CampaignsResponse>(this.url, options);
  }

  renameCampaign(campaign: Campaign): Observable<CampaignsResponse> {
    const body = {campaignId: campaign.campaignId.toString(), campaignName: campaign.campaignName};
    return this.http.put<any>(this.url, body)
    .pipe(retry(3), mergeMap(() => this.getAllCampaigns()));
  }

  /**
   * @return an Observable map mapping formIds to formNames
   */
  getCampaignNameMap(): Observable<Map<number, string>> {
    return this.getAllCampaigns().pipe(map((res: CampaignsResponse) => {
      const campaigns: Campaign[] = res.campaigns;
      const map: Map<number, string> = new Map();
      for (const campaign of campaigns) {
        map.set(campaign.campaignId, campaign.campaignName);
      }
      return map;
    }));
  }
}
