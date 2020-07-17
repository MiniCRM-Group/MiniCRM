export interface Campaign {
    campaignId: number,
    campaignName: string;
    date: string;
}

export interface CampaignsResponse {
    campaigns: Campaign[];
}
