import React from 'react';
import { useFormik } from 'formik';
import * as Yup from 'yup';
import { login } from '../api/auth';
import { useNavigate } from 'react-router-dom';
import {
  Card,
  CardContent,
  Typography,
  Button,
  TextField,
  Box,
  Stack,
  useTheme
} from '@mui/material';

const LoginPage = () => {
  const navigate = useNavigate();
  const theme = useTheme();
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
        navigate('/dashboard');
        window.location.reload(); // Force reload to update isAuthenticated in App
      } catch (err: any) {
        setErrors({ password: 'Invalid credentials' });
      } finally {
        setSubmitting(false);
      }
    },
  });

  return (
    <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center', minHeight: '100vh', background: theme.palette.background.default }}>
      <Card sx={{ maxWidth: 500, width: '100%', borderRadius: 4, boxShadow: 6 }}>
        <CardContent>
          <Box textAlign="center" mb={3}>
            <img src="/logo192.png" alt="Logo" style={{ width: 64, height: 64, marginBottom: 8 }} />
            <Typography variant="h4" fontWeight={700} color="primary.dark" gutterBottom>
              Maintenance Portal
            </Typography>
            <Typography variant="subtitle1" color="text.secondary" mb={2}>
              Sign in to your account
            </Typography>
          </Box>
          <form onSubmit={formik.handleSubmit}>
            <Stack spacing={2}>
              <TextField
                id="username"
                name="username"
                label="Username"
                fullWidth
                value={formik.values.username}
                onChange={formik.handleChange}
                error={formik.touched.username && Boolean(formik.errors.username)}
                helperText={formik.touched.username && formik.errors.username}
                autoComplete="username"
                autoFocus
              />
              <TextField
                id="password"
                name="password"
                label="Password"
                type="password"
                fullWidth
                value={formik.values.password}
                onChange={formik.handleChange}
                error={formik.touched.password && Boolean(formik.errors.password)}
                helperText={formik.touched.password && formik.errors.password}
                autoComplete="current-password"
              />
              <Button
                type="submit"
                variant="contained"
                color="primary"
                fullWidth
                sx={{ fontWeight: 600, borderRadius: 2 }}
                disabled={formik.isSubmitting}
              >
                {formik.isSubmitting ? 'Signing in...' : 'Login'}
              </Button>
              {formik.errors.password && !formik.touched.password && (
                <Typography color="error" align="center" sx={{ mt: 1 }}>
                  {formik.errors.password}
                </Typography>
              )}
            </Stack>
          </form>
        </CardContent>
      </Card>
    </Box>
  );
};

export default LoginPage; 