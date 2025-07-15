import React, { useState, useEffect } from 'react';
import { useFormik } from 'formik';
import * as Yup from 'yup';
import { getAllRoles, createRole, updateRole, deleteRole } from '../api/roles';
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
  Box,
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

const validationSchema = Yup.object({
  name: Yup.string().required('Name is required'),
  description: Yup.string().required('Description is required'),
});

const AdminRoleManagementPage: React.FC = () => {
  const [roles, setRoles] = useState<{ id: number; name: string; description: string }[]>([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [editRole, setEditRole] = useState<null | { id: number; name: string; description: string }>(null);

  useEffect(() => {
    setLoading(true);
    getAllRoles().then(data => {
      setRoles(data);
      setLoading(false);
    });
  }, []);

  const openAddModal = () => {
    setEditRole(null);
    setModalOpen(true);
  };

  const openEditModal = (role: { id: number; name: string; description: string }) => {
    setEditRole(role);
    setModalOpen(true);
  };

  const closeModal = () => {
    setModalOpen(false);
    setEditRole(null);
    formik.resetForm();
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('Are you sure you want to delete this role?')) {
      await deleteRole(id);
      setRoles(rs => rs.filter(r => r.id !== id));
    }
  };

  const formik = useFormik({
    initialValues: {
      name: editRole ? editRole.name : '',
      description: editRole ? editRole.description : '',
    },
    enableReinitialize: true,
    validationSchema,
    onSubmit: async (values) => {
      if (editRole) {
        const updated = await updateRole(editRole.id, values);
        setRoles(rs => rs.map(r => r.id === editRole.id ? updated : r));
      } else {
        const created = await createRole(values);
        setRoles(rs => [...rs, created]);
      }
      closeModal();
    },
  });

  const theme = useTheme();

  return (
    <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center', minHeight: '80vh', background: theme.palette.background.default }}>
      <Card sx={{ width: '100%', maxWidth: 1200, borderRadius: 4, boxShadow: 6 }}>
        <CardContent>
          <Stack direction="row" alignItems="center" justifyContent="space-between" mb={3}>
            <Box>
              <Typography variant="h4" fontWeight={700} color="primary.dark" gutterBottom>
                Role Management
              </Typography>
              <Typography variant="subtitle1" color="text.secondary">
                Manage user roles and permissions
              </Typography>
            </Box>
            <Button variant="contained" color="primary" onClick={openAddModal} sx={{ borderRadius: 2, fontWeight: 600, px: 3, py: 1 }}>
              Add Role
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
                    <TableCell>Description</TableCell>
                    <TableCell align="center">Actions</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {roles.map(role => (
                    <TableRow key={role.id} hover>
                      <TableCell>{role.id}</TableCell>
                      <TableCell>{role.name}</TableCell>
                      <TableCell>{role.description}</TableCell>
                      <TableCell align="center">
                        <Tooltip title="Edit">
                          <IconButton color="primary" onClick={() => openEditModal(role)}>
                            <EditIcon />
                          </IconButton>
                        </Tooltip>
                        <Tooltip title="Delete">
                          <IconButton color="error" onClick={() => handleDelete(role.id)}>
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
        <DialogTitle>{editRole ? 'Edit Role' : 'Add Role'}</DialogTitle>
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
                id="description"
                name="description"
                label="Description"
                fullWidth
                value={formik.values.description}
                onChange={formik.handleChange}
                error={formik.touched.description && Boolean(formik.errors.description)}
                helperText={formik.touched.description && formik.errors.description}
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

export default AdminRoleManagementPage; 