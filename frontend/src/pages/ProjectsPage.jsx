import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { apiFetch } from '../api';
import { useAuth } from '../context/AuthContext';

function ProjectsPage() {
  const { auth, user } = useAuth();
  const [projects, setProjects] = useState([]);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [form, setForm] = useState({
    name: '',
    description: '',
    status: 'ACTIVE',
    startDate: '',
    endDate: ''
  });

  async function loadProjects() {
    try {
      setError('');
      const data = await apiFetch('/api/projects/me', { auth });
      setProjects(data);
    } catch (err) {
      setError(err.message);
    }
  }

  useEffect(() => {
    loadProjects();
  }, []);

  function handleChange(e) {
    setForm(prev => ({
      ...prev,
      [e.target.name]: e.target.value
    }));
  }

  async function handleCreateProject(e) {
    e.preventDefault();
    setError('');
    setSuccess('');

    try {
      await apiFetch('/api/projects', {
        method: 'POST',
        body: form,
        auth
      });

      setSuccess('Project created successfully.');
      setForm({
        name: '',
        description: '',
        status: 'ACTIVE',
        startDate: '',
        endDate: ''
      });
      loadProjects();
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <div className="grid-layout">
      <div className="card">
        <h1>My Projects</h1>
        {error && <p className="error-text">{error}</p>}
        {success && <p className="success-text">{success}</p>}

        <div className="project-list">
          {projects.length === 0 ? (
            <p className="muted">No projects found.</p>
          ) : (
            projects.map(project => (
              <div className="list-item" key={project.id}>
                <div>
                  <h3>{project.name}</h3>
                  <p>{project.description || 'No description'}</p>
                  <small>Status: {project.status} | Archived: {String(project.archived)}</small>
                </div>
                <Link className="secondary-btn" to={`/projects/${project.id}`}>
                  Open
                </Link>
              </div>
            ))
          )}
        </div>
      </div>

      {user?.role === 'PROJECT_LEADER' && (
        <div className="card">
          <h2>Create Project</h2>
          <form onSubmit={handleCreateProject} className="form-grid">
            <input
              name="name"
              placeholder="Project name"
              value={form.name}
              onChange={handleChange}
            />

            <textarea
              name="description"
              placeholder="Description"
              value={form.description}
              onChange={handleChange}
            />

            <select name="status" value={form.status} onChange={handleChange}>
              <option value="PLANNED">PLANNED</option>
              <option value="ACTIVE">ACTIVE</option>
              <option value="COMPLETED">COMPLETED</option>
            </select>

            <input type="date" name="startDate" value={form.startDate} onChange={handleChange} />
            <input type="date" name="endDate" value={form.endDate} onChange={handleChange} />

            <button type="submit">Create Project</button>
          </form>
        </div>
      )}
    </div>
  );
}

export default ProjectsPage;