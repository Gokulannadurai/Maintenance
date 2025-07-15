
// import api from './axios';

// export const login = (username: string, password: string) =>
//   api.post('/auth/login', { username, password }); 

// Mock login function for frontend development
export const login = (username: string, password: string) => {
  return new Promise<{ data: { token: string; roles: string[] } }>((resolve, reject) => {
    setTimeout(() => {
      if (username && password) {
        // You can customize roles based on username if needed
        resolve({
          data: {
            token: 'mock-jwt-token',
            roles: ['EMPLOYEE', 'ADMIN'],
          },
        });
      } else {
        reject(new Error('Invalid credentials'));
      }
    }, 500);
  });
}; 