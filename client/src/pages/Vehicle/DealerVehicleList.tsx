import React, { useState, useEffect } from "react";
import { Link, useNavigate } from 'react-router-dom';
import { useCookie } from "../../contexts/CookieContext";
import { useAuth } from '../../contexts/AuthContext';
import { messageManager } from "../../services/MessageManager";

const API_LINK = process.env.REACT_APP_API_VEHICLE;


interface Account {
  accountId: string; // Assuming this is the ID of the buyer
  username: string; 
  email: string;
  firstName: string;
  lastName: string;
}
interface Vehicle {
  vehicleId: string; // Ensure vehicleId is included in the interface
  year: string; // Change to string if you want to allow empty strings
  make: string;
  model: string;
  engineType: string;
  color: string;
  price: number; // Numeric input
  condition: "Used" | "New"; // Dropdown for "Used" or "New"
  imgUrl?: string | null;
  buyer?: Account;
  dealer?: Account;
}


function DealerVehicleList(props: { dLer: boolean }) {
  const [vehicles, setVehicles] = useState<Vehicle[]>([]); // State to store fetched vehicles
  const [loading, setLoading] = useState(true); // Loading state
  const [error, setError] = useState<string | null>(null); // Error state
  const [accountId, setAccountId] = useState<string>(""); // State for account ID to filter by


  const cookie = useCookie();
  const navigate = useNavigate();

  const authContext = useAuth();
  const user = authContext.user;

  const [filters, setFilters] = useState({
    priceRange: [0, 100000],
    make: "",
    model: "",
    year: "",
    condition: ""
  });

  // Fetch vehicles from the backend
  useEffect(() => {
    const fetchVehicles = async () => {
      try {
        setLoading(true);
        const response = await fetch(`${API_LINK}/inventory`);


        if (!response.ok) {
          throw new Error("Failed to fetch data");
        }
        const data = await response.json();
        console.log("Fetched file: ", data)
        
        setVehicles(data); // Update state with fetched vehicles
      } catch (err: any) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchVehicles();
  }, []);


  const handleUpdate = (vehicleId: string) => {
    navigate(`/dashboard/vehiclelist/update-vehicle/${vehicleId}`);
  };


  const handleDelete = async (vehicleId: string) => {
    if (window.confirm("Are you sure you want to delete this vehicle?")) {
      try {
        const response = await fetch(`${API_LINK}/${vehicleId}`, {
          method: 'DELETE',
          headers: {
            "Content-Type": "application/json",
            "Authorization": cookie.cookieData.token
          },
        });

        if (!response.ok) {
          throw new Error("Failed to delete vehicle");
        }

        // Remove the deleted vehicle from the state
        setVehicles((prevVehicles) =>
          prevVehicles.filter(vehicle => vehicle.vehicleId !== vehicleId) // Correctly filter by vehicleId
        );
      } catch (err: any) {
        setError(err.message);
      }
    }
  };

  const filteredVehicles = vehicles.filter((vehicle: Vehicle) => {
    return (
      vehicle.price >= filters.priceRange[0] &&
      vehicle.price <= filters.priceRange[1] &&
      (filters.make
        ? vehicle.make.toLowerCase().includes(filters.make.toLowerCase())
        : true) &&
      (filters.model
        ? vehicle.model.toLowerCase().includes(filters.model.toLowerCase())
        : true) &&
      (filters.year ? vehicle.year === filters.year : true) &&
      (filters.condition
        ? vehicle.condition.toLowerCase() === filters.condition.toLowerCase()
        : true) &&
      (props.dLer || vehicle.buyer?.accountId === user?.userid) // Apply buyer filtering only if props.dLer is true
    );
  });
  return (
    <div className="VehicleInventory">
      {loading ? (
        <p>Loading vehicles...</p>
      ) : error ? (
        <p className="text-danger">{error}</p>
      ) : (
        <div className="row">
          <div className="col-md-12">
            <div className="row">
              {filteredVehicles.map((vehicle: Vehicle) => (
                <div className="col-md-4 mb-4" key={vehicle.vehicleId}>
                  <div className="card text-light">
                  {!props.dLer &&(
                    <Link to={`/vehicle/${vehicle.vehicleId}`}>
                      <img
                        src={vehicle.imgUrl && vehicle.imgUrl.trim() !== "" ? vehicle.imgUrl : "/AutoLinkNoImage.png"}
                        className="card-img-top"
                        alt={vehicle.model || "No Image Available"}
                      />
                      <div className="card-body">
                        <h5 className="card-title">
                          {vehicle.make} {vehicle.model}
                        </h5>
                        <p className="card-text">Price: ${vehicle.price}</p>
                        <p className="card-text">Year: {vehicle.year}</p>
                        <p className="card-text">
                          Condition: {vehicle.condition}
                        </p>

                      </div>
                    </Link>)}
                    {props.dLer &&(
                    <Link to={`/dealerVehicle/${vehicle.vehicleId}`}>
                      <img
                        src={vehicle.imgUrl && vehicle.imgUrl.trim() !== "" ? vehicle.imgUrl : "/AutoLinkNoImage.png"}
                        className="card-img-top"
                        alt={vehicle.model || "No Image Available"}
                      />
                      <div className="card-body">
                        <h5 className="card-title">
                          {vehicle.make} {vehicle.model}
                        </h5>
                        <p className="card-text">Price: ${vehicle.price}</p>
                        <p className="card-text">Year: {vehicle.year}</p>
                        <p className="card-text">
                          Condition: {vehicle.condition}
                        </p>

                      </div>
                    </Link>)}

                    {/* <button className="btn btn-warning ms-2 me-2" onClick={() => handleUpdate(vehicle.vehicleId)}>Update</button>
                    <button className="btn btn-danger ms-2 m-2" onClick={() => handleDelete(vehicle.vehicleId)}>Delete</button> */}

                    {props.dLer && (
                      <>
                        <button
                          className="btn btn-warning ms-2 me-2"
                          onClick={() => handleUpdate(vehicle.vehicleId)}
                        >
                          Update
                        </button>
                        <button
                          className="btn btn-danger ms-2 m-2"
                          onClick={() => handleDelete(vehicle.vehicleId)}
                        >
                          Delete
                        </button>
                      </>)}
                      {!props.dLer &&(
                        <>
                       <button
                      style={{
                        position: "absolute",
                        bottom: "20px",
                        right: "20px",
                        width: "60px",
                        height: "60px",
                        backgroundColor: "var(--color-text-link)",
                        color: "white",
                        border: "none",
                        borderRadius: "50%",
                        boxShadow: "0 4px 8px rgba(0, 0, 0, 0.2)",
                        cursor: "pointer",
                        fontSize: "18px",
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center"
                      }}
                       onClick={(e) => {
                                    let dealerid = e.currentTarget.getAttribute("vehicle-dealerid");
                                    let dealerusername = e.currentTarget.getAttribute("vehicle-dealerusername");
                      
                                    if(dealerid && dealerusername)
                                    messageManager.createMessageChat(parseInt(dealerid), dealerusername);
                                  }} // Replace with actual chat functionality
                                  vehicle-dealerid={vehicle.dealer ? vehicle.dealer.accountId : -1}
                                  vehicle-dealerusername={vehicle.dealer ? vehicle.dealer.username : null}
                                >
                                  💬
                                </button>
                    </>)}
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default DealerVehicleList;