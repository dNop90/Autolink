//link for authenticating api login
const API_LINK = process.env.REACT_APP_API_VEHICLE
const car_api_link = process.env.CAR_API_LINK
const car_api_token = process.env.CAR_API_TOKEN
const car_api_secret = process.env.CAR_API_SECRET

const fetchJWT = async () => {
    try {
      const response = await fetch(`${car_api_link}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
        },
        body: JSON.stringify({
          api_token: `${car_api_token}`,
          api_secret: `${car_api_secret}`,
        }),
      });
  
      if (!response.ok) {
        throw new Error("Failed to authenticate");
      }
  
      const data = await response.json();
      console.log("JWT:", data);
      return data; // JWT token
    } catch (error) {
      console.error("Error fetching JWT:", error);
      return null;
    }
  };
export default fetchJWT;  