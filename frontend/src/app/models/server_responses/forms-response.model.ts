export interface Form {
    formId: number,
    formName: string;
    googleKey: string;
    verified: boolean;
    date: string;
}

export interface FormsResponse {
    webhookUrl: string;
    forms: Form[];
}
