// Mock data
let mockRoles = [
  { id: 1, name: 'EMPLOYEE', description: 'Regular employee' },
  { id: 2, name: 'ADMIN', description: 'Administrator' },
  { id: 3, name: 'SUPER_ADMIN', description: 'Super administrator' },
];

function delay(ms: number) {
  return new Promise(res => setTimeout(res, ms));
}

export async function getAllRoles() {
  await delay(300);
  return [...mockRoles];
}

export async function createRole(role: { name: string; description: string }) {
  await delay(300);
  const newId = Math.max(0, ...mockRoles.map(r => r.id)) + 1;
  const newRole = { id: newId, ...role };
  mockRoles.push(newRole);
  return newRole;
}

export async function updateRole(id: number, role: { name: string; description: string }) {
  await delay(300);
  mockRoles = mockRoles.map(r => r.id === id ? { ...r, ...role } : r);
  return { id, ...role };
}

export async function deleteRole(id: number) {
  await delay(300);
  mockRoles = mockRoles.filter(r => r.id !== id);
  return { success: true };
} 