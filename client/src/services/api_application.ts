import axios from "axios";
export class api_application {
    api_link: string;

    constructor(api_link : string)
    {
        this.api_link = api_link;
    }

    async listPendingApplications(token: string) {
        const response = await axios({
            method: 'GET',
            url: `${this.api_link}/applications`,
            headers: {
                'Authorization' : token,
            }
        })
        return response.data;
    }

    async updateApplication(token: string, applicationId: number, status: string) {
        const response = await axios({
            method: 'PATCH',
            url: `${this.api_link}/approve/${applicationId}/${status}`,
            headers: {
                'Authorization' : token,
            }
        })
        return response.data;
    }
}
