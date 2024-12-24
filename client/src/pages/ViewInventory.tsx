import React, { useState } from "react";
import vehiclesData from "../data/VehiclesTest.json"; // Import the dummy data

import '../styles/ViewInventory.css'
const ViewInventory: React.FC = () => {
  const [filters, setFilters] = useState({
    priceRange: [0, 100000],
    make: '',
    model: '',
    year: '',
    condition: ''
  });

  const handleFilterChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFilters({
      ...filters,
      [name]: value
    });
  };

  const handleConditionChange = (value: string) => {
    setFilters({
      ...filters,
      condition: value
    });
  };

  const filteredVehicles = vehiclesData.filter(vehicle => {
    return (
      vehicle.price >= filters.priceRange[0] &&
      vehicle.price <= filters.priceRange[1] &&
      (filters.make ? vehicle.make.toLowerCase().includes(filters.make.toLowerCase()) : true) &&
      (filters.model ? vehicle.model.toLowerCase().includes(filters.model.toLowerCase()) : true) &&
      (filters.year ? vehicle.year.toString() === filters.year : true) &&
      (filters.condition ? vehicle.condition.toLowerCase() === filters.condition.toLowerCase() : true)
    );
  });

  return (
    <div  style={{ backgroundColor: '#000000', color: 'white' , padding:"35px"}}>
      <header className="text-center mb-5">
        <h1 className="display-4 fw-bold text-light">Welcome to AutoLink!</h1>
        <p className="lead text-light">
          Explore our curated selection of vehicles and connect with trusted dealers.
        </p>
      </header>

      {/* Hero Section */}
      <div
        className="bg-dark rounded-3 p-4 mb-5 text-center d-flex align-items-center justify-content-center"
        style={{
          backgroundImage: "url('https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?fit=crop&w=1600&q=80')",
          backgroundSize: "cover",
          backgroundPosition: "50% 70%",
          color: "white",
          textShadow: "2px 2px 5px rgba(0, 0, 0, 0.7)",
          height: "300px",
        }}
      >
        <div>
          <h2 className="fw-bold">Your Dream Vehicle Awaits</h2>
          <p>Find the perfect car to match your lifestyle and budget.</p>
        </div>
      </div>

      <h1 className="my-4">Vehicle Listings</h1>
      <div className="row">
        <div className="col-md-3">
          <div className="mb-4">
            <input type="text" name="make" placeholder="Make" onChange={handleFilterChange} className="form-control mb-2" style={{ backgroundColor: '#495057', color: 'white' }} />
            <input type="text" name="model" placeholder="Model" onChange={handleFilterChange} className="form-control mb-2" style={{ backgroundColor: '#495057', color: 'white' }} />
            
            <div className="row mb-2">
              <div className="col">
                <input type="number" name="minPrice" placeholder="Min Price" onChange={(e) => setFilters({ ...filters, priceRange: [e.target.value ? Number(e.target.value) : 0, filters.priceRange[1]] })} className="form-control" style={{ backgroundColor: '#495057', color: 'white' }} />
              </div>
              <div className="col">
                <input type="number" name="maxPrice" placeholder="Max Price" onChange={(e) => setFilters({ ...filters, priceRange: [filters.priceRange[0], e.target.value ? Number(e.target.value) : 100000] })} className="form-control" style={{ backgroundColor: '#495057', color: 'white' }} />
              </div>
            </div>

            <input type="text" name="year" placeholder="Year" onChange={handleFilterChange} className="form-control mb-2" style={{ backgroundColor: '#495057', color: 'white' }} />

            <div className="mb-2">
              <label htmlFor ="condition" className="form-label text-light">Condition</label>
              <select
                id="condition"
                className="form-select"
                onChange={(e) => handleConditionChange(e.target.value)}
                value={filters.condition}
                style={{ backgroundColor: '#495057', color: 'white' }}
              >
                <option value="">Select Condition</option>
                <option value="New">New</option>
                <option value="Used">Used</option>
              </select>
            </div>
          </div>
        </div>
        
        <div className="col-md-9">
          <div className="row">
            {filteredVehicles.map(vehicle => (
              <div className="col-md-4 mb-4" key={vehicle.vehicleId}>
                <div className="card bg-dark text-light">
                  <img src={vehicle.imageUrls[0]} className="card-img-top" alt={vehicle.model} />
                  <div className="card-body">
                    <h5 className="card-title">{vehicle.make} {vehicle.model}</h5>
                    <p className="card-text">Price: ${vehicle.price}</p>
                    <p className="card-text">Year: {vehicle.year}</p>
                    <p className="card-text">Condition: {vehicle.condition}</p>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ViewInventory;