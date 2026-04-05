import { useEffect, useState } from 'react';
import { apiFetch } from '../api';
import { useAuth } from '../context/AuthContext';

function UsersPage() {
  const { auth, user } = useAuth();
  const [users, setUsers] = useState([]);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [form, setForm] = useState({
    username: '',
    email: '',
    password: '',
    roleName: 'EMPLOYEE'
  });

  async function loadUsers() {
    try {
      const data = await apiFetch('/api/users', { auth });
      setUsers(data);
    } catch (err) {
      setError(err.message);
    }
  }

  useEffect(() => {
    loadUsers();
  }, []);

  function handleChange(e) {
    setForm(prev => ({
      ...prev,
      [e.target.name]: e.target.value
    }));
  }

  async function handleCreateUser(e) {
    e.preventDefault();
    setError('');
    setSuccess('');

    try {
      await apiFetch('/api/users', {
        method: 'POST',
        body: form,
        auth
      });

      setSuccess('User created successfully.');
      setForm({
        username: '',
        email: '',
        password: '',
        roleName: 'EMPLOYEE'
      });
      loadUsers();
    } catch (err) {
      setError(err.message);
    }
  }

  if (user?.role !== 'ADMIN' && user?.role !== 'PROJECT_LEADER') {
    return (
      <div className="card">
        <p className="error-text">You are not allowed to access this page.</p>
      </div>
    );
  }

  return (
    <div className="grid-layout">
      <div className="card">
        <h1>Users</h1>
        {error && <p className="error-text">{error}</p>}
        {success && <p className="success-text">{success}</p>}

        <div className="project-list">
          {users.map(u => (
            <div className="list-item" key={u.id}>
              <div>
                <h3>{u.username}</h3>
                <p>{u.email}</p>
                <small>Role: {u.roleName} | Active: {String(u.active)}</small>
              </div>
            </div>
          ))}
        </div>
      </div>

      {user?.role === 'ADMIN' && (
        <div className="card">
          <h2>Create User</h2>
          <form onSubmit={handleCreateUser} className="form-grid">
            <input
              name="username"
              placeholder="Username"
              value={form.username}
              onChange={handleChange}
            />

            <input
              name="email"
              placeholder="Email"
              value={form.email}
              onChange={handleChange}
            />

            <input
              type="password"
              name="password"
              placeholder="Password"
              value={form.password}
              onChange={handleChange}
            />

            <select name="roleName" value={form.roleName} onChange={handleChange}>
              <option value="ADMIN">ADMIN</option>
              <option value="PROJECT_LEADER">PROJECT_LEADER</option>
              <option value="EMPLOYEE">EMPLOYEE</option>
            </select>

            <button type="submit">Create User</button>
          </form>
        </div>
      )}
    </div>
  );
}

export default UsersPage;