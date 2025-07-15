export interface MaintenanceRequestDTO {
  id: number;
  title: string;
  status: string;
  priority: string;
  createdAt?: string;
}

export interface NotificationDTO {
  id: number;
  message: string;
  isRead: boolean;
  createdAt?: string;
} 