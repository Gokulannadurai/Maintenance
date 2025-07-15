// Mock data
let mockUsers = [
  { id: 1, name: 'John Doe', email: 'john.doe@example.com', roles: ['EMPLOYEE'], isActive: true },
  { id: 2, name: 'Jane Admin', email: 'jane.admin@example.com', roles: ['ADMIN'], isActive: true },
  { id: 3, name: 'Super User', email: 'super.user@example.com', roles: ['SUPER_ADMIN'], isActive: false },
];

function delay(ms: number) {
  return new Promise(res => setTimeout(res, ms));
}

export async function getAllUsers() {
  await delay(300);
  return [...mockUsers];
}

export async function createUser(user: { name: string; email: string; roles: string[]; isActive: boolean }) {
  await delay(300);
  const newId = Math.max(0, ...mockUsers.map(u => u.id)) + 1;
  const newUser = { id: newId, ...user };
  mockUsers.push(newUser);
  return newUser;
}

export async function updateUser(id: number, user: { name: string; email: string; roles: string[]; isActive: boolean }) {
  await delay(300);
  mockUsers = mockUsers.map(u => u.id === id ? { ...u, ...user } : u);
  return { id, ...user };
}

export async function deleteUser(id: number) {
  await delay(300);
  mockUsers = mockUsers.filter(u => u.id !== id);
  return { success: true };
} 