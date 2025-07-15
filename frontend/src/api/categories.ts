// Mock data
let mockCategories = [
  { id: 1, name: 'Electrical', description: 'Electrical issues' },
  { id: 2, name: 'Plumbing', description: 'Plumbing issues' },
];

function delay(ms: number) {
  return new Promise(res => setTimeout(res, ms));
}

export async function getAllCategories() {
  await delay(300);
  return [...mockCategories];
}

export async function createCategory(category: { name: string; description: string }) {
  await delay(300);
  const newId = Math.max(0, ...mockCategories.map(c => c.id)) + 1;
  const newCat = { id: newId, ...category };
  mockCategories.push(newCat);
  return newCat;
}

export async function updateCategory(id: number, category: { name: string; description: string }) {
  await delay(300);
  mockCategories = mockCategories.map(c => c.id === id ? { ...c, ...category } : c);
  return { id, ...category };
}

export async function deleteCategory(id: number) {
  await delay(300);
  mockCategories = mockCategories.filter(c => c.id !== id);
  return { success: true };
} 