export interface Form {
    readonly formId: number,
    readonly formName: string;
    readonly googleKey: string;
    readonly verified: boolean;
    readonly date: string;
}

export interface FormsResponse {
    webhookUrl: string;
    forms: Form[];
}
