const API_BASE_URL = 'http://localhost:8080';

function buildAuthHeader(auth) {
  if (!auth?.username || !auth?.password) return {};
  return {
    Authorization: `Basic ${btoa(`${auth.username}:${auth.password}`)}`
  };
}

export async function apiFetch(path, options = {}) {
  const { method = 'GET', body, auth } = options;

  const headers = {
    ...buildAuthHeader(auth)
  };

  if (body) {
    headers['Content-Type'] = 'application/json';
  }

  const response = await fetch(`${API_BASE_URL}${path}`, {
    method,
    headers,
    body: body ? JSON.stringify(body) : undefined
  });

  const contentType = response.headers.get('content-type') || '';
  const data = contentType.includes('application/json')
    ? await response.json()
    : await response.text();

  if (!response.ok) {
    const message =
      (typeof data === 'object' && data?.message) ||
      (typeof data === 'object' && data?.errors && JSON.stringify(data.errors)) ||
      data ||
      `HTTP ${response.status}`;
    throw new Error(message);
  }

  return data;
}