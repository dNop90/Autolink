import axios from "axios";
export class api_application {
    api_link: string;

    constructor(api_link: string) {
        this.api_link = api_link;
    }

    async listPendingApplications(token: string) {
        const response = await axios({
            method: 'GET',
            url: `${this.api_link}/applications`,
            headers: {
                'Authorization': token,
            }
        })
        return response.data;
    }

    async updateApplication(token: string, applicationId: number, status: string) {
        const response = await axios({
            method: 'PATCH',
            url: `${this.api_link}/approve/${applicationId}/${status}`,
            headers: {
                'Authorization': token,
            }
        })
        return response.data;
    }

    async submitApplication(token: string, applicantId: number,
        email: string, firstName: string, lastName: string,
        businessLicense: string,
        street: string, city: string, state: string, zipCode: number) {

        const response = await axios({
            method: 'POST',
            url: `${this.api_link}/apply`,
            data: {
                "applicantId": applicantId,
                "applicantEmail": email,
                "applicantFirstName": firstName,
                "applicantLastName": lastName,
                "businessLicense": businessLicense,
                "street": street,
                "city": city,
                "state": state,
                "zipCode": zipCode
            },
            headers: {
                'Authorization': token,
            }
        })
        return response.data;
    }

    async listApplicantApplication(token: string, accountId: number) {
        const response = await axios({
            method: 'GET',
            url: `${this.api_link}/application/${accountId}`,
            headers: {
                'Authorization': token,
            }
        })
        return response.data;
    }
}
