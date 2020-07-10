export interface Form {
    formId: number,
    formName: string;
    date: string;
}

export interface FormsResponse {
    forms: Form[];
}
