export interface    ShortenedUrl {
  id: string;
  url: string;
  shortCode: string;
  createdAt: string;
  updatedAt: string;
}

export interface ShortenedUrlStats extends ShortenedUrl {
  accessCount: number;
}

export interface CreateUrlRequest {
  url: string;
}

export interface UpdateUrlRequest {
  url: string;
}

export interface ApiError {
  message: string;
  status: number;
}
