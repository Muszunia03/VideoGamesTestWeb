import './SignUpForm.css';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function SignUpForm() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [email, setEmail] = useState('');
  const navigate = useNavigate(); // <---

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log('Username:', username);
    console.log('Password:', password);
    console.log('Email:', email);
    // tu później dodasz logikę do Spring Boot
  };

  return (
    <form className="login-form" onSubmit={handleSubmit}>
<input
  type="email"
  placeholder="Email"
  value={email}
  onChange={(e) => setEmail(e.target.value)}
  required
/>
      <input
        type="text"
        placeholder="Username"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
        required
      />
      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        required
      />
      <div className="button-group">
        <button type="button" onClick={() => navigate('/')}>Sign Up</button>
        <p>Oops, i misslicked, go back to login screen!</p>
        <button type="button2" onClick={() => navigate('/')}>login</button>
      </div>
    </form>
  );
}

export default SignUpForm;
