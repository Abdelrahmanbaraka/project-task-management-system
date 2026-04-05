import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

function LoginPage() {
  const { login } = useAuth();
  const navigate = useNavigate();

  const [form, setForm] = useState({
    username: '',
    password: ''
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  function handleChange(e) {
    setForm(prev => ({
      ...prev,
      [e.target.name]: e.target.value
    }));
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      await login(form.username, form.password);
      navigate('/projects');
    } catch (err) {
      setError('Login failed. Check username and password.');
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="card auth-card">
      <h1>Login</h1>
      <p className="muted">Use one of the seeded accounts to sign in.</p>

      <form onSubmit={handleSubmit} className="form-grid">
        <input
          name="username"
          placeholder="Username"
          value={form.username}
          onChange={handleChange}
        />

        <input
          type="password"
          name="password"
          placeholder="Password"
          value={form.password}
          onChange={handleChange}
        />

        <button type="submit" disabled={loading}>
          {loading ? 'Logging in...' : 'Login'}
        </button>
      </form>

      {error && <p className="error-text">{error}</p>}

      <div className="demo-box">
        <strong>Demo accounts</strong>
        <p>admin / Admin123!</p>
        <p>leader / Leader123!</p>
        <p>employee / Employee123!</p>
      </div>
    </div>
  );
}

export default LoginPage;