// Mock data
let mockLocations = [
  { id: 1, name: 'Conference Room', description: 'Main conference room' },
  { id: 2, name: 'Main Lobby', description: 'Entrance lobby' },
];

function delay(ms: number) {
  return new Promise(res => setTimeout(res, ms));
}

export async function getAllLocations() {
  await delay(300);
  return [...mockLocations];
}

export async function createLocation(location: { name: string; description: string }) {
  await delay(300);
  const newId = Math.max(0, ...mockLocations.map(l => l.id)) + 1;
  const newLoc = { id: newId, ...location };
  mockLocations.push(newLoc);
  return newLoc;
}

export async function updateLocation(id: number, location: { name: string; description: string }) {
  await delay(300);
  mockLocations = mockLocations.map(l => l.id === id ? { ...l, ...location } : l);
  return { id, ...location };
}

export async function deleteLocation(id: number) {
  await delay(300);
  mockLocations = mockLocations.filter(l => l.id !== id);
  return { success: true };
} 