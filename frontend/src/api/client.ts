import { API_BASE_URL } from "../constants/app";
import type { OverviewResponse, Notice } from "../types";

export async function fetchOverview(): Promise<OverviewResponse> {
  const response = await fetch(`${API_BASE_URL}/overview`, {
    headers: { Accept: "application/json" },
  });

  if (!response.ok) {
    throw new Error(`Overview request failed: ${response.status}`);
  }

  return response.json() as Promise<OverviewResponse>;
}

export async function fetchNotices(): Promise<Notice[]> {
  const response = await fetch(`${API_BASE_URL}/notices`, {
    headers: { Accept: "application/json" },
  });

  if (!response.ok) {
    throw new Error(`Notices request failed: ${response.status}`);
  }

  return response.json() as Promise<Notice[]>;
}

export async function createNotice(data: {
  title: string;
  content: string;
  level?: string;
  isActive?: boolean;
  sortOrder?: number;
}): Promise<Notice> {
  const response = await fetch(`${API_BASE_URL}/notices`, {
    method: "POST",
    headers: { "Content-Type": "application/json", Accept: "application/json" },
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    throw new Error(`Create notice failed: ${response.status}`);
  }

  return response.json() as Promise<Notice>;
}

export async function updateNotice(
  id: number,
  data: {
    title: string;
    content: string;
    level?: string;
    isActive?: boolean;
    sortOrder?: number;
  }
): Promise<Notice> {
  const response = await fetch(`${API_BASE_URL}/notices/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json", Accept: "application/json" },
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    throw new Error(`Update notice failed: ${response.status}`);
  }

  return response.json() as Promise<Notice>;
}

export async function deleteNotice(id: number): Promise<void> {
  const response = await fetch(`${API_BASE_URL}/notices/${id}`, {
    method: "DELETE",
    headers: { Accept: "application/json" },
  });

  if (!response.ok) {
    throw new Error(`Delete notice failed: ${response.status}`);
  }
}
