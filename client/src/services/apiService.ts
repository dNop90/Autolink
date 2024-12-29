export const authenticate = async (): Promise<string | null> => {
  try {
    const response = await fetch("http://localhost:8080/api/auth/login", {
      method: "POST",
    });

    if (!response.ok) {
      throw new Error("Failed to authenticate");
    }

    const jwt = await response.text(); // JWT is returned as plain text
    localStorage.setItem("jwt", jwt); // Save JWT to localStorage for reuse
    return jwt;
  } catch (error) {
    console.error("Error fetching JWT:", error);
    return null;
  }
};

export const fetchData = async (endpoint: string): Promise<any> => {
  const jwt = localStorage.getItem("jwt"); // Retrieve JWT from localStorage

  if (!jwt) {
    throw new Error("Missing JWT. Authenticate first.");
  }

  try {
    const response = await fetch(`http://localhost:8080/api/${endpoint}`, {
      headers: {
        Authorization: `Bearer ${jwt}`,
      },
    });

    if (!response.ok) {
      throw new Error(`Failed to fetch data from ${endpoint}`);
    }

    return await response.json();
  } catch (error) {
    console.error("Error fetching data:", error);
    return null;
  }
};
