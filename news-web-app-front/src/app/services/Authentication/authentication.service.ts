import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { JWTTokenService } from '../JWTToken/jwttoken.service';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  loggedIn: BehaviorSubject<boolean>;
  baseURL: string;

  constructor(private http: HttpClient, private tokenService: JWTTokenService) {
    this.loggedIn = new BehaviorSubject<boolean>(this.isLoggedIn());
    this.baseURL = 'http://localhost:8060/AccountAuthenticationService';
  }

  isLoggedIn(): boolean {
    return !this.tokenService.isTokenExpired();
  }

  logOut(): void {
    this.loggedIn.next(false);
    this.tokenService.reset();
  }

  async login(username: string, password: string) {
    return new Promise((resolve, reject) => {
      this.http
        .post(
          this.baseURL +
            '/users/signin?username=' +
            username +
            '&password=' +
            password,
          null,
          { responseType: 'text' }
        )
        .toPromise()
        .then((token) => {
          this.tokenService.setToken(token);
          this.tokenService.decodeToken();
          this.loggedIn.next(true);
          resolve(200);
        })
        .catch((errorCode) => {
          if (errorCode instanceof HttpErrorResponse) {
            reject(errorCode.status);
          } else {
            reject(500);
          }
        });
    });
  }

  getUsername(): string {
    return this.tokenService.getUser();
  }

  getEmail(): string {
    return this.tokenService.getEmailId();
  }
}
