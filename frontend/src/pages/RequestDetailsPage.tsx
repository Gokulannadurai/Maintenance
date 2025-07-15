import React, { useEffect, useState } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import { MaintenanceRequestDTO } from '../api/types';
import { getAllRequests, updateRequest, deleteRequest } from '../api/requests';
import { useFormik } from 'formik';
import * as Yup from 'yup';
import {
  Card,
  CardContent,
  Typography,
  Button,
  Box,
  Stack,
  Chip,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Paper,
  useTheme
} from '@mui/material';

// Extend DTO for editing (since mock API expects more fields)
type EditableRequest = MaintenanceRequestDTO & {
  description: string;
  categoryId: number;
  locationId: number;
  requesterId: number;
};

const validationSchema = Yup.object({
  title: Yup.string().required('Title is required'),
  description: Yup.string().required('Description is required'),
});

const RequestDetailsPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [request, setRequest] = useState<EditableRequest | null>(null);
  const [loading, setLoading] = useState(true);
  const [editModal, setEditModal] = useState(false);
  const theme = useTheme();

  useEffect(() => {
    setLoading(true);
    getAllRequests().then(data => {
      // For mock API, fallback description/category/location/requester
      const req = data.find(r => r.id === Number(id));
      if (req) {
        setRequest({
          ...req,
          description: (req as any).description || '',
          categoryId: (req as any).categoryId || 1,
          locationId: (req as any).locationId || 1,
          requesterId: (req as any).requesterId || 1,
        });
      } else {
        setRequest(null);
      }
      setLoading(false);
    });
  }, [id]);

  const openEditModal = () => setEditModal(true);
  const closeEditModal = () => {
    setEditModal(false);
    formik.resetForm();
  };

  const handleCancel = async () => {
    if (window.confirm('Are you sure you want to cancel this request?')) {
      await deleteRequest(Number(id));
      navigate('/requests');
    }
  };

  const formik = useFormik({
    initialValues: {
      title: request?.title || '',
      description: request?.description || '',
    },
    enableReinitialize: true,
    validationSchema,
    onSubmit: async (values) => {
      if (request) {
        const updated = await updateRequest(request.id, {
          ...request,
          title: values.title,
          description: values.description,
        });
        setRequest({ ...request, ...updated });
        closeEditModal();
      }
    },
  });

  if (loading) return <Typography align="center" sx={{ py: 5 }}>Loading...</Typography>;
  if (!request) return <Typography align="center" sx={{ py: 5 }}>Request not found.</Typography>;

  return (
    <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center', minHeight: '80vh', background: theme.palette.background.default }}>
      <Card sx={{ width: '100%', maxWidth: 900, borderRadius: 4, boxShadow: 6 }}>
        <CardContent>
          <Stack direction="row" alignItems="center" justifyContent="space-between" mb={3}>
            <Typography variant="h4" fontWeight={700} color="primary.dark" gutterBottom>
              Request Details
            </Typography>
            <Button component={Link} to="/requests" variant="outlined" color="primary">
              &larr; Back to List
            </Button>
          </Stack>
          <Stack spacing={2} mb={3}>
            <Box>
              <Typography variant="subtitle2" color="text.secondary">ID:</Typography>
              <Typography variant="body1">{request.id}</Typography>
            </Box>
            <Box>
              <Typography variant="subtitle2" color="text.secondary">Title:</Typography>
              <Typography variant="body1">{request.title}</Typography>
            </Box>
            <Box>
              <Typography variant="subtitle2" color="text.secondary">Status:</Typography>
              <Chip label={request.status} color={request.status === 'OPEN' ? 'primary' : request.status === 'IN_PROGRESS' ? 'warning' : 'default'} size="small" />
            </Box>
            <Box>
              <Typography variant="subtitle2" color="text.secondary">Priority:</Typography>
              <Chip label={request.priority} color={request.priority === 'HIGH' ? 'error' : 'default'} size="small" />
            </Box>
            <Box>
              <Typography variant="subtitle2" color="text.secondary">Created At:</Typography>
              <Typography variant="body1">{request.createdAt ? new Date(request.createdAt).toLocaleString() : ''}</Typography>
            </Box>
            <Box>
              <Typography variant="subtitle2" color="text.secondary">Description:</Typography>
              <Paper variant="outlined" sx={{ p: 2, minHeight: 40, background: '#fff' }}>{request.description || ''}</Paper>
            </Box>
          </Stack>
          {request.status === 'OPEN' && (
            <Stack direction="row" spacing={2}>
              <Button onClick={openEditModal} variant="contained" color="warning">Edit</Button>
              <Button onClick={handleCancel} variant="contained" color="error">Cancel Request</Button>
            </Stack>
          )}
        </CardContent>
      </Card>
      {/* Edit Modal */}
      <Dialog open={editModal} onClose={closeEditModal} maxWidth="sm" fullWidth>
        <DialogTitle>Edit Request</DialogTitle>
        <form onSubmit={formik.handleSubmit}>
          <DialogContent dividers>
            <Stack spacing={2}>
              <TextField
                id="title"
                name="title"
                label="Title"
                fullWidth
                value={formik.values.title}
                onChange={formik.handleChange}
                error={formik.touched.title && Boolean(formik.errors.title)}
                helperText={formik.touched.title && formik.errors.title}
                autoFocus
              />
              <TextField
                id="description"
                name="description"
                label="Description"
                fullWidth
                multiline
                minRows={3}
                value={formik.values.description}
                onChange={formik.handleChange}
                error={formik.touched.description && Boolean(formik.errors.description)}
                helperText={formik.touched.description && formik.errors.description}
              />
            </Stack>
          </DialogContent>
          <DialogActions>
            <Button onClick={closeEditModal} color="secondary" variant="outlined">Cancel</Button>
            <Button type="submit" color="primary" variant="contained">Save</Button>
          </DialogActions>
        </form>
      </Dialog>
    </Box>
  );
};

export default RequestDetailsPage; 