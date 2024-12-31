import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { api } from "../../services/api";

const VehicleDetail: React.FC = () => {
  const { vehicleId } = useParams<{ vehicleId: string }>();
  const [vehicle, setVehicle] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const API_LINK = process.env.REACT_APP_API_VEHICLE
  useEffect(() => {
    const fetchVehicle = async () => {
      try {
        setLoading(true);
        const response = await fetch(`${API_LINK}/${vehicleId}`);
        if (!response.ok) {
          throw new Error("Failed to fetch vehicle details");
        }
        const data = await response.json();
        setVehicle(data);
      } catch (err: any) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchVehicle();
  }, [vehicleId]);

  if (loading) return <p>Loading...</p>;
  if (error) return <p className="text-danger">{error}</p>;

  return (
    <div style={{ padding: "35px", maxWidth: "800px", margin: "auto" }}>
  <h1 className="text-center mb-4">{vehicle.make} {vehicle.model}</h1>
  
  <img
    src="https://images.pexels.com/photos/35967/mini-cooper-auto-model-vehicle.jpg?cs=srgb&dl=pexels-pixabay-35967.jpg&fm=jpg"
    alt={`${vehicle.make} ${vehicle.model}`}
    style={{ width: "100%", height: "auto", borderRadius: "8px", boxShadow: "0 4px 8px rgba(0, 0, 0, 0.2)" }}
  />
  
  <div className="mt-4">
    <h3>Details</h3>
    <p><strong>Price:</strong> ${vehicle.price.toLocaleString()}</p>
    <p><strong>Year:</strong> {vehicle.year}</p>
    <p><strong>Condition:</strong> {vehicle.condition}</p>
    <p><strong>In Stock:</strong> {vehicle.inStock ? "Yes" : "No"}</p>
    <p><strong>Description:</strong> {vehicle.description || "No description available."}</p>
  </div>
</div>
  );
};

export default VehicleDetail;
