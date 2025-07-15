import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import Dashboard from './pages/Dashboard';
import RequestListPage from './pages/RequestListPage';
import NewRequestPage from './pages/NewRequestPage';
import RequestDetailsPage from './pages/RequestDetailsPage';
import AdminUserManagementPage from './pages/AdminUserManagementPage';
import AdminCategoryManagementPage from './pages/AdminCategoryManagementPage';
import AdminLocationManagementPage from './pages/AdminLocationManagementPage';
import AdminRoleManagementPage from './pages/AdminRoleManagementPage';
import Layout from './Layout';

function App() {
  const isAuthenticated = !!localStorage.getItem('jwt');

  return (
    <Router>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route
          path="/dashboard"
          element={isAuthenticated ? <Layout><Dashboard /></Layout> : <Navigate to="/login" />}
        />
        <Route
          path="/requests"
          element={isAuthenticated ? <Layout><RequestListPage /></Layout> : <Navigate to="/login" />}
        />
        <Route
          path="/requests/new"
          element={isAuthenticated ? <Layout><NewRequestPage /></Layout> : <Navigate to="/login" />}
        />
        <Route
          path="/requests/:id"
          element={isAuthenticated ? <Layout><RequestDetailsPage /></Layout> : <Navigate to="/login" />}
        />
        <Route
          path="/admin/users"
          element={isAuthenticated ? <Layout><AdminUserManagementPage /></Layout> : <Navigate to="/login" />}
        />
        <Route
          path="/admin/categories"
          element={isAuthenticated ? <Layout><AdminCategoryManagementPage /></Layout> : <Navigate to="/login" />}
        />
        <Route
          path="/admin/locations"
          element={isAuthenticated ? <Layout><AdminLocationManagementPage /></Layout> : <Navigate to="/login" />}
        />
        <Route
          path="/admin/roles"
          element={isAuthenticated ? <Layout><AdminRoleManagementPage /></Layout> : <Navigate to="/login" />}
        />
        <Route path="*" element={<Navigate to={isAuthenticated ? '/dashboard' : '/login'} />} />
      </Routes>
    </Router>
  );
}

export default App;
