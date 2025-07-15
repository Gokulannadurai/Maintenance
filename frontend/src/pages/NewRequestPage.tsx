import React from 'react';
import { useFormik } from 'formik';
import * as Yup from 'yup';
import {
  Card,
  CardContent,
  Typography,
  Button,
  TextField,
  Box,
  Stack,
  Select,
  MenuItem,
  InputLabel,
  FormControl,
  FormHelperText,
  useTheme
} from '@mui/material';

// Mock categories and locations
const mockCategories = [
  { id: 1, name: 'Electrical' },
  { id: 2, name: 'Plumbing' },
];
const mockLocations = [
  { id: 1, name: 'Conference Room' },
  { id: 2, name: 'Main Lobby' },
];

const NewRequestPage: React.FC = () => {
  const theme = useTheme();
  const formik = useFormik({
    initialValues: {
      title: '',
      description: '',
      categoryId: '',
      locationId: '',
      file: null as File | null,
    },
    validationSchema: Yup.object({
      title: Yup.string().required('Title is required'),
      description: Yup.string().required('Description is required'),
      categoryId: Yup.string().required('Category is required'),
      locationId: Yup.string().required('Location is required'),
    }),
    onSubmit: (values) => {
      // For now, just log the values
      console.log('Submitted:', values);
      alert('Request submitted! (mock)');
    },
  });

  return (
    <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center', minHeight: '80vh', background: theme.palette.background.default }}>
      <Card sx={{ width: '100%', maxWidth: 700, borderRadius: 4, boxShadow: 6 }}>
        <CardContent>
          <Box textAlign="center" mb={3}>
            <img src="/logo192.png" alt="Logo" style={{ width: 48, height: 48, marginBottom: 8 }} />
            <Typography variant="h4" fontWeight={700} color="primary.dark" gutterBottom>
              New Maintenance Request
            </Typography>
          </Box>
          <form onSubmit={formik.handleSubmit}>
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
              <FormControl fullWidth error={formik.touched.categoryId && Boolean(formik.errors.categoryId)}>
                <InputLabel id="categoryId-label">Category</InputLabel>
                <Select
                  labelId="categoryId-label"
                  id="categoryId"
                  name="categoryId"
                  value={formik.values.categoryId}
                  label="Category"
                  onChange={formik.handleChange}
                >
                  <MenuItem value=""><em>Select Category</em></MenuItem>
                  {mockCategories.map(cat => (
                    <MenuItem key={cat.id} value={cat.id}>{cat.name}</MenuItem>
                  ))}
                </Select>
                {formik.touched.categoryId && formik.errors.categoryId && (
                  <FormHelperText>{formik.errors.categoryId}</FormHelperText>
                )}
              </FormControl>
              <FormControl fullWidth error={formik.touched.locationId && Boolean(formik.errors.locationId)}>
                <InputLabel id="locationId-label">Location</InputLabel>
                <Select
                  labelId="locationId-label"
                  id="locationId"
                  name="locationId"
                  value={formik.values.locationId}
                  label="Location"
                  onChange={formik.handleChange}
                >
                  <MenuItem value=""><em>Select Location</em></MenuItem>
                  {mockLocations.map(loc => (
                    <MenuItem key={loc.id} value={loc.id}>{loc.name}</MenuItem>
                  ))}
                </Select>
                {formik.touched.locationId && formik.errors.locationId && (
                  <FormHelperText>{formik.errors.locationId}</FormHelperText>
                )}
              </FormControl>
              <Button
                variant="contained"
                component="label"
                color="secondary"
                sx={{ alignSelf: 'flex-start' }}
              >
                Upload Attachment
                <input
                  id="file"
                  name="file"
                  type="file"
                  hidden
                  onChange={e => formik.setFieldValue('file', e.currentTarget.files ? e.currentTarget.files[0] : null)}
                />
              </Button>
              <Button
                type="submit"
                variant="contained"
                color="primary"
                fullWidth
                sx={{ fontWeight: 600, borderRadius: 2 }}
                disabled={formik.isSubmitting}
              >
                {formik.isSubmitting ? 'Submitting...' : 'Submit Request'}
              </Button>
            </Stack>
          </form>
        </CardContent>
      </Card>
    </Box>
  );
};

export default NewRequestPage; 