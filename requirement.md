# Office Internal Maintenance Web Application Requirements

## 1. Overview
This document outlines the requirements for an internal web application to manage office maintenance requests. The system will streamline the process for employees to submit maintenance issues, allow the admin/maintenance team to respond and resolve requests, and provide tracking and reporting capabilities.

## 2. User Roles
- **Employee**: Submits maintenance requests, tracks status, views request history.
- **Admin/Maintenance Team**: Manages incoming requests, updates status, communicates with employees, closes requests, generates reports.
- **Super Admin (optional)**: Manages users, system settings, and has full access to all data and reports.

## 3. Main Features
### 3.1 Employee Portal
- Submit new maintenance requests (with category, description, location, optional photo attachment)
- View status of submitted requests
- Edit or cancel pending requests
- View request history
- Receive notifications/updates on request status

### 3.2 Admin/Maintenance Team Portal
- Dashboard of all incoming requests (filter by status, priority, date, etc.)
- Assign requests to team members
- Update request status (e.g., Open, In Progress, On Hold, Closed)
- Communicate with employees (comments, status updates)
- Attach resolution notes and files
- Generate and export reports (by date, category, status, etc.)

### 3.3 Super Admin Portal (optional)
- Manage users and roles
- Configure system settings (categories, locations, notification preferences)
- Access all reports and audit logs

### 3.4 General Features
- Authentication and authorization (role-based access)
- Responsive UI (desktop and mobile)
- Notification system (email and/or in-app)
- Audit trail for all actions

## 4. Functional Requirements
- FR1: Employees can log in and submit maintenance requests.
- FR2: Admins can view, assign, and update requests.
- FR3: Employees receive notifications when request status changes.
- FR4: Admins can generate reports based on various filters.
- FR5: The system supports file attachments for requests and resolutions.
- FR6: All actions are logged for audit purposes.
- FR7: The application supports role-based access control.

## 5. Non-Functional Requirements
- NFR1: The backend must use Java JDK 21.
- NFR2: The frontend must use React.
- NFR3: The system should be secure (OWASP Top 10 compliance recommended).
- NFR4: The application should be performant (response time < 2s for all main actions).
- NFR5: The UI should be user-friendly and accessible (WCAG 2.1 AA compliance recommended).
- NFR6: The system should be maintainable and extensible for future features.
- NFR7: The application should support at least 100 concurrent users.

## 6. Assumptions
- The application will be deployed on the company’s internal network/cloud.
- All users have company email addresses for authentication.
- Maintenance categories and locations are configurable by admins.

## 7. Out of Scope
- Integration with external vendors or third-party maintenance providers.
- Mobile app (native) development (web responsive only).

## 8. Future Enhancements (Optional)
- SLA tracking and escalation
- Analytics dashboard
- Integration with company asset management systems
- Mobile push notifications
