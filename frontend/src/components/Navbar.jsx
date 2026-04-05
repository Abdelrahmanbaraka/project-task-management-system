import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

function Navbar() {
  const { isAuthenticated, user, logout } = useAuth();
  const navigate = useNavigate();

  function handleLogout() {
    logout();
    navigate('/login');
  }

  return (
    <nav className="navbar">
      <div className="navbar-left">
        <Link to="/projects" className="brand">Task Manager</Link>
      </div>

      {isAuthenticated && (
        <div className="navbar-right">
          <Link to="/projects">Projects</Link>
          {(user?.role === 'ADMIN' || user?.role === 'PROJECT_LEADER') && (
            <Link to="/users">Users</Link>
          )}
          <span className="user-badge">
            {user?.username} ({user?.role})
          </span>
          <button className="danger-btn" onClick={handleLogout}>Logout</button>
        </div>
      )}
    </nav>
  );
}

export default Navbar;