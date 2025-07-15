import React, { useState, useEffect } from 'react';
import { useFormik } from 'formik';
import * as Yup from 'yup';
import { getAllUsers, createUser, updateUser, deleteUser } from '../api/users';
import {
  Card,
  CardContent,
  Typography,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Checkbox,
  FormControlLabel,
  FormGroup,
  Box,
  Chip,
  Stack,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  IconButton,
  Tooltip,
  useTheme
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';

const allRoles = ['EMPLOYEE', 'ADMIN', 'SUPER_ADMIN'];

const validationSchema = Yup.object({
  name: Yup.string().required('Name is required'),
  email: Yup.string().email('Invalid email').required('Email is required'),
  roles: Yup.array().min(1, 'At least one role is required'),
});

const AdminUserManagementPage: React.FC = () => {
  const [users, setUsers] = useState<{ id: number; name: string; email: string; roles: string[]; isActive: boolean }[]>([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [editUser, setEditUser] = useState<null | { id: number; name: string; email: string; roles: string[]; isActive: boolean }>(null);

  useEffect(() => {
    setLoading(true);
    getAllUsers().then(data => {
      setUsers(data);
      setLoading(false);
    });
  }, []);

  const openAddModal = () => {
    setEditUser(null);
    setModalOpen(true);
  };

  const openEditModal = (user: { id: number; name: string; email: string; roles: string[]; isActive: boolean }) => {
    setEditUser(user);
    setModalOpen(true);
  };

  const closeModal = () => {
    setModalOpen(false);
    setEditUser(null);
    formik.resetForm();
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('Are you sure you want to delete this user?')) {
      await deleteUser(id);
      setUsers(us => us.filter(u => u.id !== id));
    }
  };

  const formik = useFormik({
    initialValues: {
      name: editUser ? editUser.name : '',
      email: editUser ? editUser.email : '',
      roles: editUser ? editUser.roles : [],
      isActive: editUser ? editUser.isActive : true,
    },
    enableReinitialize: true,
    validationSchema,
    onSubmit: async (values) => {
      if (editUser) {
        const updated = await updateUser(editUser.id, values);
        setUsers(us => us.map(u => u.id === editUser.id ? updated : u));
      } else {
        const created = await createUser(values);
        setUsers(us => [...us, created]);
      }
      closeModal();
    },
  });

  const theme = useTheme();

  return (
    <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center', minHeight: '80vh', background: theme.palette.background.default }}>
      <Card sx={{ width: '100%', maxWidth: 1400, borderRadius: 4, boxShadow: 6 }}>
        <CardContent>
          <Stack direction="row" alignItems="center" justifyContent="space-between" mb={3}>
            <Box>
              <Typography variant="h4" fontWeight={700} color="primary.dark" gutterBottom>
                User Management
              </Typography>
              <Typography variant="subtitle1" color="text.secondary">
                Manage users, assign roles, and control access
              </Typography>
            </Box>
            <Button variant="contained" color="primary" onClick={openAddModal} sx={{ borderRadius: 2, fontWeight: 600, px: 3, py: 1 }}>
              Add User
            </Button>
          </Stack>
          {loading ? (
            <Typography align="center" sx={{ py: 5 }}>Loading...</Typography>
          ) : (
            <TableContainer component={Paper} sx={{ borderRadius: 3, boxShadow: 2 }}>
              <Table>
                <TableHead>
                  <TableRow sx={{ background: theme.palette.grey[100] }}>
                    <TableCell>ID</TableCell>
                    <TableCell>Name</TableCell>
                    <TableCell>Email</TableCell>
                    <TableCell>Roles</TableCell>
                    <TableCell>Status</TableCell>
                    <TableCell align="center">Actions</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {users.map(user => (
                    <TableRow key={user.id} hover>
                      <TableCell>{user.id}</TableCell>
                      <TableCell>{user.name}</TableCell>
                      <TableCell>{user.email}</TableCell>
                      <TableCell>
                        <Stack direction="row" spacing={1}>
                          {user.roles.map(role => (
                            <Chip key={role} label={role} color={role === 'ADMIN' ? 'primary' : role === 'SUPER_ADMIN' ? 'secondary' : 'default'} size="small" />
                          ))}
                        </Stack>
                      </TableCell>
                      <TableCell>
                        <Chip label={user.isActive ? 'Active' : 'Inactive'} color={user.isActive ? 'success' : 'default'} size="small" />
                      </TableCell>
                      <TableCell align="center">
                        <Tooltip title="Edit">
                          <IconButton color="primary" onClick={() => openEditModal(user)}>
                            <EditIcon />
                          </IconButton>
                        </Tooltip>
                        <Tooltip title="Delete">
                          <IconButton color="error" onClick={() => handleDelete(user.id)}>
                            <DeleteIcon />
                          </IconButton>
                        </Tooltip>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          )}
        </CardContent>
      </Card>
      {/* Modal Dialog */}
      <Dialog open={modalOpen} onClose={closeModal} maxWidth="sm" fullWidth>
        <DialogTitle>{editUser ? 'Edit User' : 'Add User'}</DialogTitle>
        <form onSubmit={formik.handleSubmit}>
          <DialogContent dividers>
            <Stack spacing={2}>
              <TextField
                id="name"
                name="name"
                label="Name"
                fullWidth
                value={formik.values.name}
                onChange={formik.handleChange}
                error={formik.touched.name && Boolean(formik.errors.name)}
                helperText={formik.touched.name && formik.errors.name}
                autoFocus
              />
              <TextField
                id="email"
                name="email"
                label="Email"
                fullWidth
                value={formik.values.email}
                onChange={formik.handleChange}
                error={formik.touched.email && Boolean(formik.errors.email)}
                helperText={formik.touched.email && formik.errors.email}
              />
              <FormGroup row>
                {allRoles.map(role => (
                  <FormControlLabel
                    key={role}
                    control={
                      <Checkbox
                        checked={formik.values.roles.includes(role)}
                        onChange={e => {
                          if (e.target.checked) {
                            formik.setFieldValue('roles', [...formik.values.roles, role]);
                          } else {
                            formik.setFieldValue('roles', formik.values.roles.filter((r: string) => r !== role));
                          }
                        }}
                        name={role}
                      />
                    }
                    label={role}
                  />
                ))}
              </FormGroup>
              {formik.touched.roles && formik.errors.roles && (
                <Typography color="error" variant="body2">{formik.errors.roles as string}</Typography>
              )}
              <FormControlLabel
                control={
                  <Checkbox
                    checked={formik.values.isActive}
                    onChange={formik.handleChange}
                    name="isActive"
                  />
                }
                label="Active"
              />
            </Stack>
          </DialogContent>
          <DialogActions>
            <Button onClick={closeModal} color="secondary" variant="outlined">Cancel</Button>
            <Button type="submit" color="primary" variant="contained">Save</Button>
          </DialogActions>
        </form>
      </Dialog>
    </Box>
  );
};

export default AdminUserManagementPage; 