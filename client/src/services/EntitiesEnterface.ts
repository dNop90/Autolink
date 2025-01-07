export interface Account {
    accountId: number; // Assuming this is the ID of the buyer
    username: string; 
    email: string;
    firstName: string;
    lastName: string;
}

export interface Vehicle {
    vehicleId: string; // Ensure vehicleId is included in the interface
    year: string; // Change to string if you want to allow empty strings
    make: string;
    model: string;
    engineType: string;
    color: string;
    price: number; // Numeric input
    condition: "Used" | "New"; // Dropdown for "Used" or "New"
    imgUrl?: string | null;
    buyer?: Account | null;
    dealer?: Account;
    inStock: boolean;
    description: string;
  }
