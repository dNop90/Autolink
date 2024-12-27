import axios from "axios";

/**
 * Handle all API related to vehicle
 */
export class api_vehicle {
    api_link: string;

    constructor(api_link : string)
    {
        this.api_link = api_link;
    }

    async getInventory(){
        const response = await fetch(`${this.api_link}/inventory`);
        return response;
    }
}