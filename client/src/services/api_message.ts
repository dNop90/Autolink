import axios from "axios";

/**
 * Handle all API related to message
 */
export class api_message {
    api_link: string;

    constructor(api_link : string)
    {
        this.api_link = api_link;
    }

    async send(token: string, fromID: number, toID: number, message: string)
    {
        const response = await axios.post(`${this.api_link}`, {fromID, toID, message}, {headers:{'Authorization': token}});
        return response.data;
    }
};