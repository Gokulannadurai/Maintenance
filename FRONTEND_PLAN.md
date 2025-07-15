# React Frontend Plan for Maintenance Web Application

## 1. Technology Stack
- **React 18+** (with hooks)
- **TypeScript** (for type safety)
- **React Router** (for navigation)
- **Axios** (for API calls)
- **Material-UI** or **Ant Design** (for UI components)
- **Redux Toolkit** (for global state, especially auth/user)
- **Formik + Yup** (for forms and validation)
- **JWT** (for authentication, stored in memory or HttpOnly cookie)
- **React Query** (optional, for data fetching/caching)

## 2. Folder Structure Example
```
src/
  api/                // Axios instances, API functions
  components/         // Reusable UI components
  features/           // Feature folders (users, requests, auth, etc.)
  hooks/              // Custom hooks
  pages/              // Route-level components
  routes/             // Route definitions
  store/              // Redux slices, store config
  utils/              // Utility functions
  App.tsx
  index.tsx
```

## 3. Authentication Flow
- **Login Page**: POST `/api/auth/login` with username/password.
- **Store JWT**: On success, store JWT in memory (or HttpOnly cookie for security).
- **Attach JWT**: Add `Authorization: Bearer <token>` to all protected API requests.
- **Role-based Routing**: Use roles from login response to control access (Employee/Admin/Super Admin).

## 4. Main Features & Pages
### Employee Portal
- **Dashboard**: List of user’s maintenance requests, status, notifications.
- **New Request**: Form to submit a new maintenance request (category, location, description, file upload).
- **Request Details**: View status history, comments, attachments.
- **Edit/Cancel Request**: If status is pending.
- **Notifications**: In-app notification bell, unread count, mark as read.

### Admin Portal
- **Admin Dashboard**: List/filter/search all requests.
- **Assign Requests**: Assign requests to team members.
- **Update Status**: Change status, add resolution notes/files.
- **User Management**: CRUD users, assign roles.
- **Category/Location Management**: CRUD categories/locations.
- **Reports**: Generate/export reports.

### Super Admin Portal
- **System Settings**: Manage roles, categories, locations, notification preferences.
- **Audit Logs**: View all audit logs.

## 5. API Integration Example (Axios)
**api/axios.ts**
```typescript
import axios from 'axios';

const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL || 'http://localhost:8080/api',
});

api.interceptors.request.use(config => {
  const token = localStorage.getItem('jwt'); // or from Redux
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export default api;
```

**api/auth.ts**
```typescript
import api from './axios';

export const login = (username: string, password: string) =>
  api.post('/auth/login', { username, password });
```

## 6. Sample Page: Login
**pages/LoginPage.tsx**
```tsx
import React from 'react';
import { useFormik } from 'formik';
import * as Yup from 'yup';
import { login } from '../api/auth';

const LoginPage = () => {
  const formik = useFormik({
    initialValues: { username: '', password: '' },
    validationSchema: Yup.object({
      username: Yup.string().required('Required'),
      password: Yup.string().required('Required'),
    }),
    onSubmit: async (values, { setSubmitting, setErrors }) => {
      try {
        const { data } = await login(values.username, values.password);
        localStorage.setItem('jwt', data.token);
        // Save roles, redirect, etc.
      } catch (err: any) {
        setErrors({ password: 'Invalid credentials' });
      } finally {
        setSubmitting(false);
      }
    },
  });

  return (
    <form onSubmit={formik.handleSubmit}>
      <input name="username" onChange={formik.handleChange} value={formik.values.username} />
      <input name="password" type="password" onChange={formik.handleChange} value={formik.values.password} />
      <button type="submit" disabled={formik.isSubmitting}>Login</button>
      {formik.errors.password && <div>{formik.errors.password}</div>}
    </form>
  );
};

export default LoginPage;
```

## 7. File Upload Example
**api/attachments.ts**
```typescript
import api from './axios';

export const uploadAttachment = (file: File, requestId: number) => {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('requestId', String(requestId));
  return api.post('/attachments/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
};
```

## 8. Role-based Routing Example
**routes/ProtectedRoute.tsx**
```tsx
import React from 'react';
import { Navigate } from 'react-router-dom';

const ProtectedRoute = ({ children, allowedRoles }) => {
  const token = localStorage.getItem('jwt');
  const userRoles = /* decode roles from token or Redux */;
  if (!token) return <Navigate to="/login" />;
  if (!allowedRoles.some(role => userRoles.includes(role))) return <Navigate to="/unauthorized" />;
  return children;
};
```

## 9. Notifications Example
- Poll `/api/notifications/unread?userId=...` or use WebSocket for real-time.
- Show unread count in header.
- Mark as read: `POST /api/notifications/{id}/mark-read`.

## 10. General Best Practices
- **Type all DTOs** in TypeScript to match backend.
- **Error handling**: Show user-friendly messages for all API errors.
- **Form validation**: Use Yup for all forms.
- **Security**: Never store JWT in localStorage if you can use HttpOnly cookies.
- **Accessibility**: Use semantic HTML and ARIA attributes.
- **Testing**: Use React Testing Library and Jest.

## 11. Sample DTO Type (TypeScript)
```typescript
export interface MaintenanceRequestDTO {
  id: number;
  title: string;
  description: string;
  status: string;
  priority: string;
  requesterId: number;
  requesterName?: string;
  assignedToId?: number;
  assignedToName?: string;
  categoryId: number;
  categoryName?: string;
  locationId: number;
  locationName?: string;
  createdAt?: string;
  updatedAt?: string;
}
```

## 12. Getting Started
1. **Create app**:  
   `npx create-react-app maintenance-frontend --template typescript`
2. **Install dependencies**:  
   `npm install axios react-router-dom @mui/material @emotion/react @emotion/styled formik yup`
3. **Set up routing, auth context, and API layer.**
4. **Build pages for each API resource.**
5. **Test with your backend (use dev profile).**

## 13. UI/UX
- Use a dashboard layout with a sidebar for navigation.
- Use tables for lists (requests, users, etc.).
- Use dialogs/modals for create/edit forms.
- Show loading spinners and error states.
- Make it responsive for mobile/desktop.

## 14. Example Project Structure
```
src/
  api/
    auth.ts
    users.ts
    requests.ts
    attachments.ts
    notifications.ts
    ...
  components/
    Navbar.tsx
    Sidebar.tsx
    RequestTable.tsx
    NotificationBell.tsx
    ...
  features/
    auth/
    requests/
    users/
    ...
  pages/
    LoginPage.tsx
    Dashboard.tsx
    RequestDetails.tsx
    AdminPanel.tsx
    ...
  store/
    index.ts
    authSlice.ts
    ...
  App.tsx
  index.tsx
```

## 15. Summary Table: API to UI Mapping
| API Endpoint                        | UI Page/Component                |
|--------------------------------------|----------------------------------|
| `/api/auth/login`                   | Login Page                       |
| `/api/users`                        | User Management, Registration    |
| `/api/requests`                     | Request List, Create/Edit Form   |
| `/api/attachments/upload`           | File Upload in Request Form      |
| `/api/notifications/unread`         | Notification Bell/Panel          |
| `/api/audit-logs/by-user`           | Audit Log Page (Admin)           |
| `/api/categories`                   | Category Management (Admin)      |
| `/api/locations`                    | Location Management (Admin)      |
| `/api/roles`                        | Role Management (Super Admin)    |

## 16. Next Steps
- Scaffold the project with the above structure.
- Implement authentication and protected routes first.
- Build CRUD pages for each main resource.
- Integrate file upload and notifications.
- Style with Material-UI or Ant Design.

---
**If you want a full starter codebase or a specific page/component implemented, let me know which part you want to see first!** 