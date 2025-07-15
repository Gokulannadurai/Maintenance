import React, { useEffect, useState } from 'react';
import { MaintenanceRequestDTO, NotificationDTO } from '../api/types';
import { Link, useNavigate } from 'react-router-dom';
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
  List,
  ListItem,
  ListItemText,
  ListItemSecondaryAction,
  Chip,
  Box,
  useTheme
} from '@mui/material';

const mockNotifications: NotificationDTO[] = [
  { id: 1, message: 'Your request has been assigned.', isRead: false, createdAt: '2024-07-14T11:00:00Z' },
];

const Dashboard: React.FC = () => {
  const [requests, setRequests] = useState<MaintenanceRequestDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();
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
      <Card sx={{ width: '100%', maxWidth: 1200, borderRadius: 4, boxShadow: 6 }}>
        <CardContent>
          <Stack direction="row" alignItems="center" justifyContent="space-between" mb={3}>
            <Box>
              <Typography variant="h4" fontWeight={700} color="primary.dark" gutterBottom>
                Dashboard
              </Typography>
              <Typography variant="subtitle1" color="text.secondary">
                Welcome to the Maintenance Portal
              </Typography>
            </Box>
            <Button variant="contained" color="primary" component={Link} to="/requests" sx={{ borderRadius: 2, fontWeight: 600, px: 3, py: 1 }}>
              View All Requests
            </Button>
          </Stack>
          <Stack direction={{ xs: 'column', md: 'row' }} spacing={4}>
            <Box flex={2}>
              <Typography variant="h6" fontWeight={600} mb={2}>
                My Maintenance Requests
              </Typography>
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
                        <TableRow key={req.id} hover sx={{ cursor: 'pointer' }} onClick={() => navigate(`/requests/${req.id}`)}>
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
            </Box>
            <Box flex={1}>
              <Typography variant="h6" fontWeight={600} mb={2}>
                Unread Notifications
              </Typography>
              <Paper sx={{ borderRadius: 3, boxShadow: 2 }}>
                <List>
                  {mockNotifications.map(n => (
                    <ListItem key={n.id} sx={{ opacity: n.isRead ? 0.5 : 1 }}>
                      <ListItemText
                        primary={n.message}
                        secondary={n.createdAt ? new Date(n.createdAt).toLocaleString() : ''}
                      />
                    </ListItem>
                  ))}
                  {mockNotifications.length === 0 && (
                    <ListItem>
                      <ListItemText primary="No unread notifications." />
                    </ListItem>
                  )}
                </List>
              </Paper>
            </Box>
          </Stack>
        </CardContent>
      </Card>
    </Box>
  );
};

export default Dashboard; 