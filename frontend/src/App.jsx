import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import ProjectsPage from './pages/ProjectsPage';
import ProjectDetailPage from './pages/ProjectDetailPage';
import UsersPage from './pages/UsersPage';
import ProtectedRoute from './components/ProtectedRoute';
import Navbar from './components/Navbar';

function App() {
  return (
    <BrowserRouter>
      <Navbar />
      <div className="page-container">
        <Routes>
          <Route path="/login" element={<LoginPage />} />

          <Route
            path="/projects"
            element={
              <ProtectedRoute>
                <ProjectsPage />
              </ProtectedRoute>
            }
          />

          <Route
            path="/projects/:projectId"
            element={
              <ProtectedRoute>
                <ProjectDetailPage />
              </ProtectedRoute>
            }
          />

          <Route
            path="/users"
            element={
              <ProtectedRoute>
                <UsersPage />
              </ProtectedRoute>
            }
          />

          <Route path="/" element={<Navigate to="/projects" replace />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;