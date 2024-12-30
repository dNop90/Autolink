import React, { useState, useEffect } from 'react';

// Define the Dealer type
interface Dealer {
  id: number;
  name: string;
  inventory: string[]; // Adjust this type based on the actual structure of the inventory
}

const DealerInventory: React.FC = () => {
  const [dealers, setDealers] = useState<Dealer[]>([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [filteredDealers, setFilteredDealers] = useState<Dealer[]>([]);

  useEffect(() => {
    // Fetch dealers
    const fetchDealers = async () => {
      try {
        const response = await fetch('/api/dealers');
        const data: Dealer[] = await response.json(); // Specify the type here
        setDealers(data);
      } catch (error) {
        console.error('Error fetching dealers:', error);
      }
    };

    fetchDealers();
  }, []);

  useEffect(() => {
    setFilteredDealers(
      dealers.filter((dealer) =>
        dealer.name.toLowerCase().includes(searchTerm.toLowerCase())
      )
    );
  }, [searchTerm, dealers]);

  return (
    <div>
      <h1>Dealers</h1>
      <input
        type="text"
        placeholder="Search dealers"
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
      />
      <ul>
        {filteredDealers.map((dealer) => (
          <li key={dealer.id}>
            <h3>{dealer.name}</h3>
            <button>View Inventory</button>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default DealerInventory;
