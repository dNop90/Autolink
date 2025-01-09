import React, { useState, useEffect } from "react";
import { Link, Navigate, useLocation, useNavigate } from 'react-router-dom';
import { useCookie } from "../../contexts/CookieContext";
import { useAuth } from '../../contexts/AuthContext';
import { messageManager } from "../../services/MessageManager";
import { Vehicle } from "../../services/EntitiesEnterface";
import '../../styles/DealerList.css'; // Import the CSS file

const API_LINK = process.env.REACT_APP_API_VEHICLE;

function DealerVehicleList(props: { dLer: boolean }) {
  const [vehicles, setVehicles] = useState<Vehicle[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const cookie = useCookie();
  const navigate = useNavigate();
  const authContext = useAuth();
  const user = authContext.user;
  const location = useLocation();

  useEffect(() => {
    const fetchVehicles = async () => {
      try {
        setLoading(true);
        const response = await fetch(`${API_LINK}/inventory`);
        if (!response.ok) {
          throw new Error("Failed to fetch data");
        }
        const data = await response.json();
        setVehicles(data);
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

        setVehicles((prevVehicles) =>
          prevVehicles.filter(vehicle => vehicle.vehicleId !== vehicleId)
        );
      } catch (err: any) {
        setError(err.message);
      }
    }
  };


  const filteredVehicles = vehicles.filter((vehicle: Vehicle) => {
    if (!props.dLer) {
      // Show vehicles where the buyer's accountId matches the user's id
      return vehicle.buyer?.accountId === user?.userid;
    } else {
      // Show vehicles where the dealer's accountId matches the user's id
      return vehicle.dealer?.accountId === user?.userid;
    }
  });

  // Check for roles
  if(user?.role === 2 && !location.pathname.includes("/dashboard/dealerVehicleList")){
    return (<Navigate to="/dashboard/dealerVehicleList" replace/>)
  }


  return (
    <div className="VehicleInventory">
      {props.dLer ? <h4>Vehicles added by you:</h4> : <h4>Vehicles You Purchased</h4>}
      
      {loading ? (
        <h3>Loading vehicles...</h3>
      ) : error ? (
        <p className="text-danger">{error}</p>
      ) : filteredVehicles.length === 0 ? (
        <p>There are no vehicles added.</p>
      ) : (
        <div className="row">
          <div className="col-md-12">
            <div className="row">
              {filteredVehicles.map((vehicle: Vehicle) => (
                <div className="col-md-4 mb-4" key={vehicle.vehicleId}>
                  <div className={`card text-light ${vehicle.buyer ? 'has-buyer' : 'no-buyer'}`}>
                    <Link to={`/vehicle/${vehicle.vehicleId}`}>
                      <img
                        src={vehicle.imgUrl && vehicle.imgUrl .trim() !== "" ? vehicle.imgUrl : "/AutoLinkNoImage.png"}
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
                        <p className="card-text">
                          Dealer: {vehicle.dealer?.firstName}
                        </p>
                        <p className="card-text">
                          Buyer: {vehicle.buyer ? vehicle.buyer.firstName : "Not Sold"}
                        </p>
                      </div>
                    </Link>
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
                      </>
                    )}
                    {!props.dLer && (
                      <button
                       className="chat-button"
                        onClick={(e) => {
                          let dealerid = e.currentTarget.getAttribute("vehicle-dealerid");
                          let dealerusername = e.currentTarget.getAttribute("vehicle-dealerusername");
                          if (dealerid && dealerusername)
                            messageManager.createMessageChat(parseInt(dealerid), dealerusername);
                        }}
                        vehicle-dealerid={vehicle.dealer ? vehicle.dealer.accountId : -1}
                        vehicle-dealerusername={vehicle.dealer ? vehicle.dealer.username : null}
                      >
                        💬
                      </button>
                    )}
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