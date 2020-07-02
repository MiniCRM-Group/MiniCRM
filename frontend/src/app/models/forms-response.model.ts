import { Form } from './form.model';
export interface FormsResponse {
    webhookUrl: string;
    forms: Form[];
}
