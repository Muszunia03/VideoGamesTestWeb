import './App.css';
import LoginForm from './LoginForm';
import SignUpForm from './SignUpForm'; // <- dodaj ten import
import { BrowserRouter, Routes, Route } from 'react-router-dom';

function App() {
  return (
    <BrowserRouter>
      <div className="logo-container">
        <img src="/Logo.png" alt="Video Game Quiz Logo" style={{ width: '150px', height: 'auto' }} />
      </div>
      <h1>Video Game Quiz</h1>
      
      <Routes>
        <Route path="/" element={<LoginForm />} />
        <Route path="/signup" element={<SignUpForm />} />
      </Routes>

      <p>Welcome to the ultimate quiz about video games!</p>
    </BrowserRouter>
  );
}

export default App;
