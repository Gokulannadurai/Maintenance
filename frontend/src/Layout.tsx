import React, { useState } from 'react';
import { AppBar, Toolbar, Typography, Drawer, List, ListItem, ListItemIcon, ListItemText, IconButton, CssBaseline, Box, Divider, useTheme, ThemeProvider, createTheme, Container } from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import DashboardIcon from '@mui/icons-material/Dashboard';
import ListAltIcon from '@mui/icons-material/ListAlt';
import CategoryIcon from '@mui/icons-material/Category';
import LocationOnIcon from '@mui/icons-material/LocationOn';
import PeopleIcon from '@mui/icons-material/People';
import SecurityIcon from '@mui/icons-material/Security';
import LogoutIcon from '@mui/icons-material/Logout';
import { useNavigate } from 'react-router-dom';
import logo from './logo.svg';

const drawerWidth = 240;

const navItems = [
  { text: 'Dashboard', icon: <DashboardIcon />, path: '/dashboard' },
  { text: 'Requests', icon: <ListAltIcon />, path: '/requests' },
  { text: 'Categories', icon: <CategoryIcon />, path: '/admin/categories' },
  { text: 'Locations', icon: <LocationOnIcon />, path: '/admin/locations' },
  { text: 'Users', icon: <PeopleIcon />, path: '/admin/users' },
  { text: 'Roles', icon: <SecurityIcon />, path: '/admin/roles' },
];

const theme = createTheme({
  palette: {
    primary: { main: '#1976d2' },
    background: { default: '#f4f6f8' },
  },
  shape: { borderRadius: 12 },
  typography: { fontFamily: 'Roboto, Arial, sans-serif' },
});

const Layout: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [open, setOpen] = useState(false);
  const navigate = useNavigate();

  const handleDrawerToggle = () => setOpen(!open);
  const handleNav = (path: string) => {
    navigate(path);
    setOpen(false);
  };
  const handleLogout = () => {
    localStorage.removeItem('jwt');
    navigate('/login');
    window.location.reload();
  };

  const drawer = (
    <div>
      <Toolbar sx={{ justifyContent: 'center' }}>
        <img src={logo} alt="Logo" style={{ width: 40, height: 40, marginRight: 8 }} />
      </Toolbar>
      <Divider />
      <List>
        {navItems.map(item => (
          <ListItem component="div" key={item.text} onClick={() => handleNav(item.path)} style={{ cursor: 'pointer' }}>
            <ListItemIcon>{item.icon}</ListItemIcon>
            <ListItemText primary={item.text} />
          </ListItem>
        ))}
      </List>
      <Divider />
      <List>
        <ListItem component="div" onClick={handleLogout} style={{ cursor: 'pointer' }}>
          <ListItemIcon><LogoutIcon /></ListItemIcon>
          <ListItemText primary="Logout" />
        </ListItem>
      </List>
    </div>
  );

  return (
    <ThemeProvider theme={theme}>
      <Box sx={{ display: 'flex' }}>
        <CssBaseline />
        <AppBar position="fixed" sx={{ zIndex: theme.zIndex.drawer + 1 }}>
          <Toolbar>
            <IconButton color="inherit" edge="start" onClick={handleDrawerToggle} sx={{ mr: 2, display: { sm: 'none' } }}>
              <MenuIcon />
            </IconButton>
            <img src={logo} alt="Logo" style={{ width: 36, height: 36, marginRight: 16, display: 'none' }} />
            <Typography variant="h6" noWrap component="div" sx={{ fontWeight: 700, letterSpacing: 1 }}>
              Maintenance Portal
            </Typography>
          </Toolbar>
        </AppBar>
        <Drawer
          variant="permanent"
          sx={{
            width: drawerWidth,
            flexShrink: 0,
            [`& .MuiDrawer-paper`]: { width: drawerWidth, boxSizing: 'border-box' },
            display: { xs: 'none', sm: 'block' },
          }}
          open
        >
          {drawer}
        </Drawer>
        <Drawer
          variant="temporary"
          open={open}
          onClose={handleDrawerToggle}
          ModalProps={{ keepMounted: true }}
          sx={{
            display: { xs: 'block', sm: 'none' },
            [`& .MuiDrawer-paper`]: { width: drawerWidth, boxSizing: 'border-box' },
          }}
        >
          {drawer}
        </Drawer>
        <Box component="main" sx={{ flexGrow: 1, p: 3, width: { sm: `calc(100% - ${drawerWidth}px)` }, minHeight: '100vh', background: theme.palette.background.default }}>
          <Toolbar />
          <Container maxWidth="lg">
            {children}
          </Container>
        </Box>
      </Box>
    </ThemeProvider>
  );
};

export default Layout; 