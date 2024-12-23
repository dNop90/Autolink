import React, { useState } from 'react';
import './DealerDashboard.css';

// Define a Vehicle type for the form data
interface Vehicle {
  vehicleId: string;
  model: string;
  inStock: boolean;
  condition: string;
  make: string;
  color: string;
  engineType: string;
}

const DealerDashboard: React.FC = () => {
  const [vehicle, setVehicle] = useState<Vehicle>({
    vehicleId: '',
    model: '',
    inStock: false,
    condition: '',
    make: '',
    color: '',
    engineType: ''
  });

  // Handle form field changes
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setVehicle((prevState) => ({
      ...prevState,
      [name]: value
    }));
  };

  // Handle checkbox for 'inStock'
  const handleInStockChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setVehicle((prevState) => ({
      ...prevState,
      inStock: e.target.checked
    }));
  };

  // Handle form submission
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // For now, we'll just log the vehicle details to the console
    console.log('New Vehicle Added:', vehicle);
    // You can replace this with an API call to save the vehicle
  };

  return (
    <div className="dealer-dashboard">
      <h1>Dealer Dashboard - Add New Vehicle</h1>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Vehicle ID:</label>
          <input
            type="text"
            name="vehicleId"
            value={vehicle.vehicleId}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>Model:</label>
          <input
            type="text"
            name="model"
            value={vehicle.model}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>In Stock:</label>
          <input
            type="checkbox"
            checked={vehicle.inStock}
            onChange={handleInStockChange}
          />
        </div>
        <div>
          <label>Condition:</label>
          <input
            type="text"
            name="condition"
            value={vehicle.condition}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>Make:</label>
          <input
            type="text"
            name="make"
            value={vehicle.make}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>Color:</label>
          <input
            type="text"
            name="color"
            value={vehicle.color}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>Engine Type:</label>
          <input
            type="text"
            name="engineType"
            value={vehicle.engineType}
            onChange={handleChange}
            required
          />
        </div>
        <button type="submit">Add Vehicle</button>
      </form>
    </div>
  );
};

export default DealerDashboard;
