const fetchJWT = async () => {
    try {
      const response = await fetch("https://carapi.app/api/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
        },
        body: JSON.stringify({
          api_token: "27588508-be3d-4695-8c6b-9a40267a8bc6",
          api_secret: "b476031dd242ed8263546b43d870c870",
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