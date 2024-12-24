import React from 'react';
import './Home.css'

const Home: React.FC = () => {
  return (
    <div className='vid'>
      <video className='vid-play' autoPlay muted loop>
        <source src='./video/BMW.mp4' type="video/mp4"/>
      </video>

      <div className='vid-context'>
        <h1>Welcome to Autolink</h1>
        <p>Explore vehicle inventories and interact with dealers</p>
      </div>
    </div>
    
  );
};

export default Home;
