import { Injectable } from '@angular/core';
import jwt_decode from 'jwt-decode';

@Injectable()
export class JWTTokenService {
  jwtToken: string;
  decodedToken: { [key: string]: string };

  constructor() {
    this.jwtToken = localStorage.getItem('JWTToken');

    if (this.jwtToken) {
      this.decodeToken();

      if (this.isTokenExpired()) {
        this.reset();
      }
    }
  }

  reset(): void {
    this.jwtToken = null;
    this.decodedToken = null;
    localStorage.removeItem('JWTToken');
  }

  setToken(token: string) {
    if (token) {
      this.jwtToken = token;
      this.decodeToken();
      localStorage.setItem('JWTToken', token);
    }
  }

  decodeToken(): void {
    if (this.jwtToken) {
      this.decodedToken = jwt_decode(this.jwtToken);
    }
  }

  getUser(): string {
    if (!this.decodedToken) {
      this.decodeToken();
    }
    return this.decodedToken ? this.decodedToken.displayname : null;
  }

  getEmailId(): string {
    if (!this.decodedToken) {
      this.decodeToken();
    }
    return this.decodedToken ? this.decodedToken.email : null;
  }

  getExpiryTime(): number {
    if (!this.decodedToken) {
      this.decodeToken();
    }
    return this.decodedToken ? parseInt(this.decodedToken.exp) : null;
  }

  isTokenExpired(): boolean {
    const expiryTime: number = this.getExpiryTime();
    if (expiryTime) {
      return 1000 * expiryTime - new Date().getTime() < 5000;
    } else {
      return true;
    }
  }
}
