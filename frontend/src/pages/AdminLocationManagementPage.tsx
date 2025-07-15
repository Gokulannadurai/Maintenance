import React, { useState, useEffect } from 'react';
import { useFormik } from 'formik';
import * as Yup from 'yup';
import { getAllLocations, createLocation, updateLocation, deleteLocation } from '../api/locations';
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

const AdminLocationManagementPage: React.FC = () => {
  const [locations, setLocations] = useState<{ id: number; name: string; description: string }[]>([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [editLocation, setEditLocation] = useState<null | { id: number; name: string; description: string }>(null);

  useEffect(() => {
    setLoading(true);
    getAllLocations().then(data => {
      setLocations(data);
      setLoading(false);
    });
  }, []);

  const openAddModal = () => {
    setEditLocation(null);
    setModalOpen(true);
  };

  const openEditModal = (loc: { id: number; name: string; description: string }) => {
    setEditLocation(loc);
    setModalOpen(true);
  };

  const closeModal = () => {
    setModalOpen(false);
    setEditLocation(null);
    formik.resetForm();
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('Are you sure you want to delete this location?')) {
      await deleteLocation(id);
      setLocations(locs => locs.filter(l => l.id !== id));
    }
  };

  const formik = useFormik({
    initialValues: {
      name: editLocation ? editLocation.name : '',
      description: editLocation ? editLocation.description : '',
    },
    enableReinitialize: true,
    validationSchema,
    onSubmit: async (values) => {
      if (editLocation) {
        const updated = await updateLocation(editLocation.id, values);
        setLocations(locs => locs.map(l => l.id === editLocation.id ? updated : l));
      } else {
        const created = await createLocation(values);
        setLocations(locs => [...locs, created]);
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
                Location Management
              </Typography>
              <Typography variant="subtitle1" color="text.secondary">
                Manage maintenance locations
              </Typography>
            </Box>
            <Button variant="contained" color="primary" onClick={openAddModal} sx={{ borderRadius: 2, fontWeight: 600, px: 3, py: 1 }}>
              Add Location
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
                  {locations.map(loc => (
                    <TableRow key={loc.id} hover>
                      <TableCell>{loc.id}</TableCell>
                      <TableCell>{loc.name}</TableCell>
                      <TableCell>{loc.description}</TableCell>
                      <TableCell align="center">
                        <Tooltip title="Edit">
                          <IconButton color="primary" onClick={() => openEditModal(loc)}>
                            <EditIcon />
                          </IconButton>
                        </Tooltip>
                        <Tooltip title="Delete">
                          <IconButton color="error" onClick={() => handleDelete(loc.id)}>
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
        <DialogTitle>{editLocation ? 'Edit Location' : 'Add Location'}</DialogTitle>
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

export default AdminLocationManagementPage; 