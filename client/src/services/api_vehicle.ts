export const authenticate = async (): Promise<string | null> => {
    try {
      const response = await fetch("http://localhost:8080/vehicles/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
      });
  
      if (!response.ok) {
        throw new Error("Failed to authenticate");
      }
  
      const data = await response.json();
      return data.jwt; // JWT Token
    } catch (error) {
      console.error("Error fetching JWT:", error);
      return null;
    }
  };
  
  export const fetchData = async (endpoint: string, jwt: string): Promise<any> => {
    try {
      const response = await fetch(`http://localhost:8080/vehicles/${endpoint}`, {
        headers: {
          Authorization: `Bearer ${jwt}`,
          Accept: "application/json",
        },
      });
  
      if (!response.ok) {
        throw new Error(`Failed to fetch data from ${endpoint}`);
      }
  
      const data = await response.json();
      return data;
    } catch (error) {
      console.error("Error fetching data:", error);
      return null;
    }
  };
  