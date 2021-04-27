import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { JWTTokenService } from '../JWTToken/jwttoken.service';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  loggedIn: BehaviorSubject<boolean>;

  constructor(private http: HttpClient, private tokenService:JWTTokenService) {
    this.loggedIn = new BehaviorSubject<boolean>(this.isLoggedIn());
   }

  isLoggedIn(){
    return !this.tokenService.isTokenExpired();
  }

  logOut(){
    this.loggedIn.next(false);
    this.tokenService.reset();
  }

  async login(username, password){

    return new Promise((resolve, reject) => {
      this.http.post("http://localhost:8060/AccountAuthenticationService/users/signin?username=" + username + "&password=" + password, null, { responseType: 'text'}).toPromise().then(token =>{
        this.tokenService.setToken(token);
        this.tokenService.decodeToken();
        this.loggedIn.next(true);
        resolve(200);
      })
      .catch(errorCode =>{
        if(errorCode instanceof HttpErrorResponse){
          reject(errorCode.status);
        }
        else{
          reject(500);
        }
      })
    });
  }

  getUsername(){
    return this.tokenService.getUser();
  }

  getEmail(){
    return this.tokenService.getEmailId();
  }
}
