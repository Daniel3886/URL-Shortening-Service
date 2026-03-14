import { ApiError, CreateUrlRequest, ShortenedUrl, ShortenedUrlStats } from "./types";

const API_BASE_URL = "http://localhost:8080";

class ApiClient {

  private baseUrl: string;

  constructor(baseUrl: string) {
    this.baseUrl = baseUrl;
  }

    private async handleResponse<T>(response: Response): Promise<T> {
    if (!response.ok) {
      const error: ApiError = {
        message: response.statusText || "An error occurred",
        status: response.status,
      };

      if (response.status === 404) {
        error.message = "Short URL not found";
      } else if (response.status === 400) {
        const data = await response.json().catch(() => ({}));
        error.message = data.message || "Invalid request";
      }

      throw error;
    }

    if (response.status === 204) {
      return {} as T;
    }

    return response.json();
  }


  async createShortUrl(data: CreateUrlRequest): Promise<ShortenedUrl> {
    const response = await fetch(`${this.baseUrl}/shorten`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    });
    return this.handleResponse<ShortenedUrl>(response);
  }

  async updateShortUrl(shortCode: string, data: CreateUrlRequest): Promise<ShortenedUrl> {
    const response = await fetch(`${this.baseUrl}/shorten/${shortCode}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    });
    return this.handleResponse<ShortenedUrl>(response);
  }

  async deleteShortUrl(shortCode: string): Promise<void> {
    const response = await fetch(`${this.baseUrl}/shorten/${shortCode}`, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
      },
    });
    return this.handleResponse<void>(response);
  }

  async getUrlStats(shortCode: string): Promise<ShortenedUrlStats> {
    const response = await fetch(`${this.baseUrl}/shorten/${shortCode}/stats`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });
    return this.handleResponse<ShortenedUrlStats>(response);
  }

   getRedirectUrl(shortCode: string): string {
    return `${this.baseUrl}/shorten/${shortCode}`;
  }  
}

 export const apiClient = new ApiClient(API_BASE_URL);

 export { ApiClient };