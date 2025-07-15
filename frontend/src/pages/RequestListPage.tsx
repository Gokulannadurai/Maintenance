import React, { useEffect, useState } from 'react';
import { MaintenanceRequestDTO } from '../api/types';
import { useNavigate } from 'react-router-dom';
import { getAllRequests } from '../api/requests';
import {
  Card,
  CardContent,
  Typography,
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Stack,
  Chip,
  Box,
  useTheme
} from '@mui/material';

const RequestListPage: React.FC = () => {
  const navigate = useNavigate();
  const [requests, setRequests] = useState<MaintenanceRequestDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const theme = useTheme();

  useEffect(() => {
    setLoading(true);
    getAllRequests().then(data => {
      setRequests(data);
      setLoading(false);
    });
  }, []);

  return (
    <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center', minHeight: '80vh', background: theme.palette.background.default }}>
      <Card sx={{ width: '100%', maxWidth: 1400, borderRadius: 4, boxShadow: 6 }}>
        <CardContent>
          <Stack direction="row" alignItems="center" justifyContent="space-between" mb={3}>
            <Box>
              <Typography variant="h4" fontWeight={700} color="primary.dark" gutterBottom>
                Maintenance Requests
              </Typography>
              <Typography variant="subtitle1" color="text.secondary">
                View and manage all maintenance requests
              </Typography>
            </Box>
            <Button variant="contained" color="primary" onClick={() => navigate('/requests/new')} sx={{ borderRadius: 2, fontWeight: 600, px: 3, py: 1 }}>
              New Request
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
                    <TableCell>Title</TableCell>
                    <TableCell>Status</TableCell>
                    <TableCell>Priority</TableCell>
                    <TableCell>Created At</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {requests.map(req => (
                    <TableRow
                      key={req.id}
                      hover
                      sx={{ cursor: 'pointer' }}
                      onClick={() => navigate(`/requests/${req.id}`)}
                    >
                      <TableCell>{req.id}</TableCell>
                      <TableCell>{req.title}</TableCell>
                      <TableCell>{req.status}</TableCell>
                      <TableCell>
                        <Chip label={req.priority} color={req.priority === 'HIGH' ? 'error' : 'default'} size="small" />
                      </TableCell>
                      <TableCell>{req.createdAt ? new Date(req.createdAt).toLocaleString() : ''}</TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          )}
        </CardContent>
      </Card>
    </Box>
  );
};

export default RequestListPage; 