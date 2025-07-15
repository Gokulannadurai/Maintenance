// Mock data
let mockRequests = [
  { id: 1, title: 'Fix AC', description: 'The AC is not working.', status: 'OPEN', priority: 'NORMAL', requesterId: 1, categoryId: 1, locationId: 1 },
  { id: 2, title: 'Fix Projector', description: 'Projector not working', status: 'IN_PROGRESS', priority: 'HIGH', requesterId: 2, categoryId: 2, locationId: 2 },
];

function delay(ms: number) {
  return new Promise(res => setTimeout(res, ms));
}

export async function getAllRequests() {
  await delay(300);
  return [...mockRequests];
}

export async function createRequest(request: { title: string; description: string; status?: string; priority?: string; requesterId: number; categoryId: number; locationId: number }) {
  await delay(300);
  const newId = Math.max(0, ...mockRequests.map(r => r.id)) + 1;
  const newReq = { id: newId, status: 'OPEN', priority: 'NORMAL', ...request };
  mockRequests.push(newReq);
  return newReq;
}

export async function updateRequest(id: number, request: { title: string; description: string; status?: string; priority?: string; requesterId: number; categoryId: number; locationId: number }) {
  await delay(300);
  mockRequests = mockRequests.map(r => r.id === id ? { ...r, ...request } : r);
  return { id, ...request };
}

export async function deleteRequest(id: number) {
  await delay(300);
  mockRequests = mockRequests.filter(r => r.id !== id);
  return { success: true };
} 