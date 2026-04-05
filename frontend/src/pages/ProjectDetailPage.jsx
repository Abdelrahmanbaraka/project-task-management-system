import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { apiFetch } from '../api';
import { useAuth } from '../context/AuthContext';

function ProjectDetailPage() {
  const { projectId } = useParams();
  const { auth, user } = useAuth();

  const [project, setProject] = useState(null);
  const [tasks, setTasks] = useState([]);
  const [progress, setProgress] = useState(null);
  const [users, setUsers] = useState([]);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const [taskForm, setTaskForm] = useState({
    title: '',
    description: '',
    status: 'OPEN',
    priority: 'MEDIUM',
    projectId: Number(projectId),
    assignedToUserId: ''
  });

  const [assignUserId, setAssignUserId] = useState('');

  async function loadProjectData() {
    try {
      setError('');
      const myProjects = await apiFetch('/api/projects/me', { auth });
      const currentProject = myProjects.find(p => String(p.id) === String(projectId));
      setProject(currentProject || null);

      const tasksData = await apiFetch(`/api/tasks/project/${projectId}`, { auth });
      setTasks(tasksData);

      if (user?.role === 'PROJECT_LEADER') {
        try {
          const progressData = await apiFetch(`/api/projects/${projectId}/progress`, { auth });
          setProgress(progressData);
        } catch {
          setProgress(null);
        }

        try {
          const usersData = await apiFetch('/api/users', { auth });
          setUsers(usersData);
        } catch {
          setUsers([]);
        }
      }
    } catch (err) {
      setError(err.message);
    }
  }

  useEffect(() => {
    loadProjectData();
  }, [projectId]);

  function handleTaskChange(e) {
    setTaskForm(prev => ({
      ...prev,
      [e.target.name]: e.target.value
    }));
  }

  async function handleCreateTask(e) {
    e.preventDefault();
    setError('');
    setSuccess('');

    try {
      await apiFetch('/api/tasks', {
        method: 'POST',
        body: {
          ...taskForm,
          assignedToUserId: taskForm.assignedToUserId ? Number(taskForm.assignedToUserId) : null,
          projectId: Number(projectId)
        },
        auth
      });

      setSuccess('Task created successfully.');
      setTaskForm({
        title: '',
        description: '',
        status: 'OPEN',
        priority: 'MEDIUM',
        projectId: Number(projectId),
        assignedToUserId: ''
      });
      loadProjectData();
    } catch (err) {
      setError(err.message);
    }
  }

  async function handleStatusChange(taskId, status) {
    try {
      await apiFetch(`/api/tasks/${taskId}/status`, {
        method: 'PATCH',
        body: { status },
        auth
      });
      loadProjectData();
    } catch (err) {
      setError(err.message);
    }
  }

  async function handleAssignMember(e) {
    e.preventDefault();
    setError('');
    setSuccess('');

    try {
      await apiFetch('/api/projects/assign-member', {
        method: 'POST',
        body: {
          projectId: Number(projectId),
          userId: Number(assignUserId)
        },
        auth
      });

      setSuccess('User assigned successfully.');
      setAssignUserId('');
      loadProjectData();
    } catch (err) {
      setError(err.message);
    }
  }

  async function handleArchiveProject() {
    try {
      await apiFetch(`/api/projects/${projectId}/archive`, {
        method: 'PUT',
        auth
      });
      setSuccess('Project archived successfully.');
      loadProjectData();
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <div className="stack-layout">
      <div className="card">
        <h1>{project?.name || `Project #${projectId}`}</h1>
        <p>{project?.description || 'No description available.'}</p>
        <p className="muted">
          Status: {project?.status} | Archived: {String(project?.archived)}
        </p>

        {user?.role === 'PROJECT_LEADER' && (
          <div className="inline-actions">
            <button className="danger-btn" onClick={handleArchiveProject}>
              Archive Project
            </button>
          </div>
        )}

        {progress && (
          <div className="progress-box">
            <h3>Project Progress</h3>
            <p>Total Tasks: {progress.totalTasks}</p>
            <p>Completed Tasks: {progress.completedTasks}</p>
            <p>Progress: {progress.progressPercentage.toFixed(1)}%</p>
          </div>
        )}
      </div>

      {error && <p className="error-text">{error}</p>}
      {success && <p className="success-text">{success}</p>}

      {(user?.role === 'PROJECT_LEADER' || user?.role === 'EMPLOYEE') && (
        <div className="card">
          <h2>Create Task</h2>
          <form onSubmit={handleCreateTask} className="form-grid">
            <input
              name="title"
              placeholder="Task title"
              value={taskForm.title}
              onChange={handleTaskChange}
            />

            <textarea
              name="description"
              placeholder="Task description"
              value={taskForm.description}
              onChange={handleTaskChange}
            />

            <select name="status" value={taskForm.status} onChange={handleTaskChange}>
              <option value="OPEN">OPEN</option>
              <option value="IN_PROGRESS">IN_PROGRESS</option>
              <option value="DONE">DONE</option>
            </select>

            <select name="priority" value={taskForm.priority} onChange={handleTaskChange}>
              <option value="LOW">LOW</option>
              <option value="MEDIUM">MEDIUM</option>
              <option value="HIGH">HIGH</option>
            </select>

            <input
              name="assignedToUserId"
              placeholder="Assigned user ID (optional)"
              value={taskForm.assignedToUserId}
              onChange={handleTaskChange}
            />

            <button type="submit">Create Task</button>
          </form>
        </div>
      )}

      {user?.role === 'PROJECT_LEADER' && (
        <div className="card">
          <h2>Assign Member</h2>
          <form onSubmit={handleAssignMember} className="form-grid">
            <select value={assignUserId} onChange={(e) => setAssignUserId(e.target.value)}>
              <option value="">Select user</option>
              {users.map(u => (
                <option key={u.id} value={u.id}>
                  {u.username} ({u.roleName})
                </option>
              ))}
            </select>

            <button type="submit">Assign to Project</button>
          </form>
        </div>
      )}

      <div className="card">
        <h2>Tasks</h2>
        <div className="project-list">
          {tasks.length === 0 ? (
            <p className="muted">No tasks found.</p>
          ) : (
            tasks.map(task => (
              <div className="list-item" key={task.id}>
                <div>
                  <h3>{task.title}</h3>
                  <p>{task.description || 'No description'}</p>
                  <small>
                    Status: {task.status} | Priority: {task.priority} | Assigned to: {task.assignedToUsername || 'Not assigned'}
                  </small>
                </div>

                {(user?.role === 'PROJECT_LEADER' || user?.role === 'EMPLOYEE') && (
                  <select
                    value={task.status}
                    onChange={(e) => handleStatusChange(task.id, e.target.value)}
                  >
                    <option value="OPEN">OPEN</option>
                    <option value="IN_PROGRESS">IN_PROGRESS</option>
                    <option value="DONE">DONE</option>
                  </select>
                )}
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
}

export default ProjectDetailPage;